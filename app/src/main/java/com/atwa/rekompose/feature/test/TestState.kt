package com.atwa.rekompose.feature.test

import android.util.Log
import com.atwa.rekompose.core.di.ServiceLocator
import com.atwa.rekompose.core.effect.affectedReducer
import com.atwa.rekompose.core.effect.withNoEffect
import com.atwa.rekompose.core.effect.withSuspendEffect
import com.atwa.rekompose.store.Action

data class TestState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val testList: List<Any> = listOf(),
)

sealed interface TestAction : Action {
    object FetchTests : TestAction
    data class FetchTestsSuccess(val testList: List<Any>) : TestAction
    data class FetchTestsFailure(val error: String) : TestAction
}

val testReducer = affectedReducer<TestState, Action> { state, effect, action ->
    Log.d("THREAD NAME : ", "Reducer running on thread ${Thread.currentThread().name}")
    when (action) {
        is TestAction.FetchTests -> state.copy(isLoading = true).withSuspendEffect {
            ServiceLocator.githubRepo.fetchTrendingRepo()
        }

        is TestAction.FetchTestsSuccess -> state.copy(
            isLoading = false,
            error = null,
            testList = action.testList.toMutableList()
        ).withNoEffect()

        is TestAction.FetchTestsFailure -> state.copy(
            isLoading = false,
            error = action.error,
            testList = listOf()
        ).withNoEffect()

        else -> state.withNoEffect()
    }
}