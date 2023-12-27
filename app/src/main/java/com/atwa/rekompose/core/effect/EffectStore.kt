package com.atwa.rekompose.core.effect

import com.atwa.rekompose.core.di.ServiceLocator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.reduxkotlin.GetState
import org.reduxkotlin.StoreEnhancer
import org.reduxkotlin.StoreSubscriber
import org.reduxkotlin.StoreSubscription
import org.reduxkotlin.TypedDispatcher
import org.reduxkotlin.TypedReducer
import org.reduxkotlin.TypedStore
import org.reduxkotlin.asTyped
import org.reduxkotlin.createStore
import org.reduxkotlin.threadsafe.createThreadSafeStore
import org.reduxkotlin.typedReducer

fun <State, Action : Any> createAffectedStore(
    reducer: AffectedReducer<State, Action>,
    preloadedState: State,
    initAction: Action,
    replaceAction: Action,
    enhancer: AffectedStoreEnhancer<State, Action>? = null,
): AffectedStore<State, Action> {
    if (enhancer != null) {
        return enhancer { r, initialState, _ ->
            createAffectedStore(
                r,
                initialState,
                initAction,
                replaceAction
            )
        }(
            reducer,
            preloadedState,
            null
        )
    }

    var currentReducer = reducer
    var currentState = preloadedState
    var currentEffect: Effect<Action> = Effect.none()
    var currentListeners = mutableListOf<() -> Unit>()
    var nextListeners = currentListeners
    var isDispatching = false

    /**
     * This makes a shallow copy of currentListeners so we can use
     * nextListeners as a temporary list while dispatching.
     *
     * This prevents any bugs around consumers calling
     * subscribe/unsubscribe in the middle of a dispatch.
     */
    fun ensureCanMutateNextListeners() {
        if (nextListeners === currentListeners) {
            nextListeners = currentListeners.toMutableList()
        }
    }

    /**
     * Reads the state tree managed by the store.
     *
     * @returns {S} The current state tree of your application.
     */
    fun getState(): State {
        check(!isDispatching) {
            """
        |You may not call store.getState() while the reducer is executing.
        |The reducer has already received the state as an argument.
        |Pass it down from the top reducer instead of reading it from the 
        |store.
        |You may be accessing getState while dispatching from another thread.
        |Try createThreadSafeStore().
        |https://reduxkotlin.org/introduction/threading
            """.trimMargin()
        }

        return currentState
    }

    /**
     * Adds a change listener. It will be called any time an action is
     * dispatched, and some part of the state tree may potentially have changed.
     * You may then call `getState()` to read the current state tree inside the
     * callback.
     *
     * You may call `dispatch()` from a change listener, with the following
     * caveats:
     *
     * 1. The subscriptions are snapshotted just before every `dispatch()` call.
     * If you subscribe or unsubscribe while the listeners are being invoked,
     * this will not have any effect on the `dispatch()` that is currently in
     * progress. However, the next `dispatch()` call, whether nested or not,
     * will use a more recent snapshot of the subscription list.
     *
     * 2. The listener should not expect to see all state changes, as the state
     * might have been updated multiple times during a nested `dispatch()`
     * before the listener is called. It is, however, guaranteed that all
     * subscribers registered before the `dispatch()` started will be called
     * with the latest state by the time it exits.
     *
     * @param {StoreSubscriber} [listener] A callback to be invoked on every
     * dispatch.
     * @returns {StoreSubscription} A fun  to remove this change listener.
     */
    fun subscribe(listener: StoreSubscriber): StoreSubscription {
        check(!isDispatching) {
            """|You may not call store.subscribe() while the reducer is executing.
             |If you would like to be notified after the store has been updated, 
             |subscribe from a component and invoke store.getState() in the 
             |callback to access the latest state. See 
             |https://www.reduxkotlin.org/api/store#subscribelistener-storesubscriber
             |for more details.
             |You may be seeing this due accessing the store from multiplethreads.
             |Try createThreadSafeStore()
             |https://reduxkotlin.org/introduction/threading
            """.trimMargin()
        }

        var isSubscribed = true

        ensureCanMutateNextListeners()
        nextListeners.add(listener)

        return {
            if (!isSubscribed) {
                Unit
            }

            check(!isDispatching) {
                """You may not unsubscribe from a store listener while the reducer
                 |is executing. See 
                 |https://www.reduxkotlin.org/api/store#subscribelistener-storesubscriber
                 |for more details.
                """.trimMargin()
            }

            isSubscribed = false

            ensureCanMutateNextListeners()
            val index = nextListeners.indexOf(listener)
            nextListeners.removeAt(index)
        }
    }

    /**
     * Dispatches an action. It is the only way to trigger a state change.
     *
     * The `reducer` function, used to create the store, will be called with the
     * current state tree and the given `action`. Its return value will
     * be considered the **next** state of the tree, and the change listeners
     * will be notified.
     *
     * The base implementation only supports plain object actions. If you want
     * to dispatch something else, such as a function or 'thunk' you need to
     * wrap your store creating function into the corresponding middleware. For
     * example, see the documentation for the `redux-thunk` package. Even the
     * middleware will eventually dispatch plain object actions using this
     * method.
     *
     * @param {Any} [action] A plain object representing “what changed”. It is
     * a good idea to keep actions serializable so you can record and replay
     * user sessions, or use the time travelling `redux-devtools`.
     *
     * @returns {Any} For convenience, the same action object you dispatched.
     *
     * Note that, if you use a custom middleware, it may wrap `dispatch()` to
     * return something else (for example, a Promise you can await).
     */
    fun dispatch(action: Action): Action {
        require(isPlainObject(obj = action)) {
            """Actions must be plain objects. Use custom middleware for async 
            |actions.
            """.trimMargin()
        }

        /*
        check(!isDispatching) {
            """You may not dispatch while state is being reduced.
            |2 conditions can cause this error:
            |    1) Dispatching from a reducer
            |    2) Dispatching from multiple threads
            |If #2 switch to createThreadSafeStore().
            |https://reduxkotlin.org/introduction/threading""".trimMargin()
        }
         */
        var affectedState: AffectedState<State, Action>? = null
        try {
            isDispatching = true
            affectedState = currentReducer(currentState, currentEffect, action)
            currentState = affectedState.state
        } finally {
            isDispatching = false
            ServiceLocator.coroutineScope.launch(Dispatchers.IO) {
                affectedState?.effect?.run()?.let { affectedAction ->
                    withContext(Dispatchers.Main) {
                        dispatch(affectedAction)
                    }
                }
            }
        }

        val listeners = nextListeners
        currentListeners = nextListeners
        listeners.forEach { it() }

        return action
    }

    /**
     * Replaces the reducer currently used by the store to calculate the state.
     *
     * You might need this if your app implements code splitting and you want to
     * load some of the reducers dynamically. You might also need this if you
     * implement a hot reloading mechanism for Redux.
     *
     * @param {function} nextReducer The reducer for the store to use instead.
     * @returns {void}
     */
    fun replaceReducer(nextReducer: AffectedReducer<State, Action>) {
        currentReducer = nextReducer

        // This action has a similar effect to ActionTypes.INIT.
        // Any reducers that existed in both the new and old rootReducer
        // will receive the previous state. This effectively populates
        // the new state tree with any relevant data from the old one.
        dispatch(replaceAction)
    }

    /**
     * Interoperability point for observable/reactive libraries.
     * @returns {observable} A minimal observable of state changes.
     * For more information, see the observable proposal:
     * https://github.com/tc39/proposal-observable
     *//* TODO: consider kotlinx.coroutines.flow?

   */

    // When a store is created, an "INIT" action is dispatched so that every
    // reducer returns their initial state. This effectively populates
    // the initial state tree.
    dispatch(initAction)

    return object : AffectedStore<State, Action> {
        override val store: AffectedStore<State, Action> = this
        override val getState: GetState<State> = ::getState
        override var dispatch: TypedDispatcher<Action> = ::dispatch
        override val subscribe: (StoreSubscriber) -> StoreSubscription = ::subscribe
        override val replaceReducer: (AffectedReducer<State, Action>) -> Unit = ::replaceReducer
    }
}


/**
 * Creates a [TypedStore]. For further details see the matching [createStore].
 */
inline fun <State, reified Action : Any> createTypedStore(
    crossinline reducer: TypedReducer<State, Action>,
    preloadedState: State,
    noinline enhancer: StoreEnhancer<State>? = null,
): TypedStore<State, Action> = createStore(
    reducer = typedReducer(reducer),
    preloadedState,
    enhancer,
).asTyped()

/**
 * Creates a thread-safe [TypedStore]. For further details see the matching [createThreadSafeStore].
 */
inline fun <State, reified Action : Any> createAffectedThreadSafeStore(
    noinline reducer: AffectedReducer<State, Action>,
    preloadedState: State,
    noinline enhancer: AffectedStoreEnhancer<State, Action>? = null,
    initAction: Action,
    replaceAction: Action,
): EffectThreadSafeStore<State, Action> =
    EffectThreadSafeStore(
        createAffectedStore(
            reducer,
            preloadedState,
            initAction,
            replaceAction,
            enhancer
        )
    )

internal fun <Action> isPlainObject(obj: Action): Boolean = obj !is Function<*>

interface AffectedStore<State, Action> {
    /**
     * Reference to the underlying untyped store
     */
    val store: AffectedStore<State, Action>

    /**
     * Current store state getter
     */
    val getState: GetState<State>

    /**
     * Dispatcher that can be used to update the store state
     */
    var dispatch: TypedDispatcher<Action>

    /**
     * Subscribes to state's updates.
     * Subscription returns [StoreSubscription] that can be invoked to unsubscribe from further updates.
     */
    val subscribe: (StoreSubscriber) -> StoreSubscription

    /**
     * Replace store's reducer with a new implementation
     */
    val replaceReducer: (AffectedReducer<State, Action>) -> Unit

    /**
     * Current store state
     */
    val state: State get() = getState()
}