package com.atwa.rekompose.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.atwa.rekompose.core.middleware.flowableMiddleware
import com.atwa.rekompose.core.middleware.loggerMiddleware
import com.atwa.rekompose.core.middleware.suspendMiddleware
import com.atwa.rekompose.di.DI.coroutineScope
import com.atwa.rekompose.feature.repositories.presentation.RepositoriesState
import com.atwa.rekompose.feature.repositories.domain.repositoriesReducer
import org.reduxkotlin.applyMiddleware
import org.reduxkotlin.compose.StoreProvider
import org.reduxkotlin.threadsafe.createTypedThreadSafeStore


@Composable
fun AppStore() = StoreProvider(
    createTypedThreadSafeStore(
        ::rootReducer,
        AppState(),
        applyMiddleware(
            flowableMiddleware(coroutineScope),
            suspendMiddleware(coroutineScope),
            loggerMiddleware(coroutineScope)
        )
    )
) {
    AppScreen()
}

fun rootReducer(state: AppState, action: Any) = AppState(
    repositories = repositoriesReducer(state.repositories, action),
)

@Immutable
data class AppState(
    val repositories: RepositoriesState = RepositoriesState(),
)


