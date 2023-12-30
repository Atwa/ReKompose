package com.atwa.rekompose.app

import androidx.compose.runtime.Composable
import com.atwa.rekompose.core.di.ServiceLocator.coroutineScope
import com.atwa.rekompose.core.middleware.loggerMiddleware
import com.atwa.rekompose.core.middleware.sideEffectMiddleware
import com.atwa.rekompose.feature.repositories.presentation.RepositoriesState
import com.atwa.rekompose.feature.repositories.presentation.repositoriesReducer
import org.reduxkotlin.applyMiddleware
import org.reduxkotlin.compose.StoreProvider
import org.reduxkotlin.threadsafe.createTypedThreadSafeStore


@Composable
fun AppStore() = StoreProvider(
    createTypedThreadSafeStore(
        ::rootReducer,
        AppState(),
        applyMiddleware(
            sideEffectMiddleware(coroutineScope),
            loggerMiddleware()
        )
    )
) {
    AppScreen()
}

fun rootReducer(state: AppState, action: Any) = AppState(
    repositories = repositoriesReducer(state.repositories, action),
)

data class AppState(
    val repositories: RepositoriesState = RepositoriesState(),
)


