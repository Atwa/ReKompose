package com.atwa.rekompose.core.effect

import com.atwa.rekompose.feature.repositories.RepositoriesAction
import com.atwa.rekompose.feature.repositories.repositoriesReducer
import com.atwa.rekompose.feature.test.TestAction
import com.atwa.rekompose.feature.test.testReducer
import com.atwa.rekompose.store.Action
import com.atwa.rekompose.store.AppState

inline fun <reified ParentState, State, Action> partialReduce(
    state: ParentState,
    effect: Effect<Action>,
    action: Action,
    reducer: AffectedReducer<State, Action>,
    selector: ParentState.() -> State,
    onCopy: ParentState.(State) -> ParentState,
): AffectedState<ParentState, Action> =
    reducer(state.selector(), effect, action).let { effectState ->
        state.onCopy(effectState.state).withEffect(effectState.effect)
    }


