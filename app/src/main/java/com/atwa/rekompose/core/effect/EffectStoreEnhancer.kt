package com.atwa.rekompose.core.effect

import org.reduxkotlin.TypedDispatcher
import org.reduxkotlin.compose

public fun <State,Action> applyAffectedMiddleware(vararg middlewares: AffectedMiddleware<State,Action>): AffectedStoreEnhancer<State,Action> {
    return { storeCreator ->
        { reducer, initialState, en: Any? ->
            val store = storeCreator(reducer, initialState, en)
            val origDispatch = store.dispatch
            val dispatch: TypedDispatcher<Action> = {
                throw IllegalStateException(
                    """Dispatching while constructing your middleware is not allowed.
                    Other middleware would not be applied to this dispatch."""
                )
            }
            store.dispatch = dispatch
            val chain = middlewares.map { middleware -> middleware(store) }
            store.dispatch = compose(chain)(origDispatch)
            store
        }
    }
}

public typealias AffectedMiddleware<State,Action> = (store: AffectedStore<State,Action>) -> (next: TypedDispatcher<Action>) -> (action: Action) -> Any
public fun <State,Action> affectedMiddleware(dispatch: (AffectedStore<State,Action>, next: TypedDispatcher<Action>, action: Action) -> Any): AffectedMiddleware<State,Action> =
    { store ->
        { next ->
            { action: Action ->
                dispatch(store, next, action)
            }
        }
    }
