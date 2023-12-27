package com.atwa.rekompose.core.effect

fun combineAffectedReducers(vararg reducers: AffectedReducer<Any, Any>):
        AffectedReducer<Any, Any> = { state, effect, action ->
    val affectedState = AffectedState(state, effect)
    reducers.fold(affectedState) { effectState, reducer ->
        reducer(
            effectState.state,
            effectState.effect,
            action
        ).withEffect(effectState.effect)
    }
}