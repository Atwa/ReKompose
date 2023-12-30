package com.atwa.rekompose.store


import androidx.compose.runtime.Composable
import com.atwa.rekompose.core.di.ServiceLocator.coroutineScope
import com.atwa.rekompose.core.effect.AffectedStoreProvider
import com.atwa.rekompose.core.effect.affectedReducer
import com.atwa.rekompose.core.effect.applyAffectedMiddleware
import com.atwa.rekompose.core.effect.createAffectedThreadSafeStore
import com.atwa.rekompose.core.effect.partialReduce
import com.atwa.rekompose.core.effect.withEffect
import com.atwa.rekompose.core.middleware.asyncMiddleware
import com.atwa.rekompose.core.middleware.coroutineDispatcherMiddleware
import com.atwa.rekompose.core.middleware.loggerMiddleware
import com.atwa.rekompose.feature.repositories.RepositoriesAction
import com.atwa.rekompose.feature.repositories.RepositoriesState
import com.atwa.rekompose.feature.repositories.repositoriesReducer
import com.atwa.rekompose.feature.test.TestAction
import com.atwa.rekompose.feature.test.TestState
import com.atwa.rekompose.feature.test.testReducer

val rootReducer = affectedReducer<AppState, Action> { state, effect, action ->
    when (action) {
        is RepositoriesAction ->
            partialReduce(
                state, effect, action, repositoriesReducer, { repositories },
                { partialState -> copy(repositories = partialState) },
            )

        is TestAction ->
            partialReduce(state, effect, action, testReducer, { test },
                { partialState -> copy(test = partialState) })

        else -> state.withEffect(effect)
    }
}

val appStore = createAffectedThreadSafeStore(
    rootReducer,
    AppState(),
    applyAffectedMiddleware(
        asyncMiddleware(coroutineScope,20000),
        coroutineDispatcherMiddleware(coroutineScope),
        loggerMiddleware()
    ),
    Action.INIT,
    Action.REPLACE
)

@Composable
fun AppStore() = AffectedStoreProvider(appStore.store) {
    AppScreen()
}


data class AppState(
    val repositories: RepositoriesState = RepositoriesState(),
    val test: TestState = TestState(),
)

interface Action {
    fun isComplete() = true
    object INIT : Action
    object REPLACE : Action
}
