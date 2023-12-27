package com.atwa.rekompose.core.effect

public typealias AffectedReducer<State, Action> = (state: State, effect: Effect<Action>, action: Action) -> AffectedState<State, Action>

public typealias AffectedStoreEnhancer<State, Action> = (AffectedStoreCreator<State, Action>) -> AffectedStoreCreator<State, Action>

public typealias AffectedStoreCreator<State, Action> = (
    reducer: AffectedReducer<State, Action>,
    initialState: State,
    enhancer: Any?,
) -> AffectedStore<State, Action>

public inline fun <State : Any, reified Action : Any> affectedReducer(
    crossinline reducer: AffectedReducer<State, Action>,
): AffectedReducer<State, Action> = { state, effect, action ->
    reducer(state, effect, action)
}

