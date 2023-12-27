package com.atwa.rekompose.core.effect

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.SynchronizedObject
import org.reduxkotlin.GetState
import org.reduxkotlin.StoreSubscriber
import org.reduxkotlin.StoreSubscription
import org.reduxkotlin.TypedDispatcher

@OptIn(InternalCoroutinesApi::class)
public class EffectThreadSafeStore<State,Action>(override val store: AffectedStore<State,Action>) :
    AffectedStore<State,Action>,
    SynchronizedObject() {
    override var dispatch: TypedDispatcher<Action> = { action ->
        synchronized(this) { store.dispatch(action) }
    }

    override val getState: GetState<State> = {
        synchronized(this) { store.getState() }
    }

    override val replaceReducer: (AffectedReducer<State, Action>) -> Unit = { reducer ->
        synchronized(this) { store.replaceReducer(reducer) }
    }

    override val subscribe: (StoreSubscriber) -> StoreSubscription = { storeSubscriber ->
        synchronized(this) { store.subscribe(storeSubscriber) }
    }
}
