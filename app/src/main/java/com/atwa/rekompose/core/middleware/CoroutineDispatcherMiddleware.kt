package com.atwa.rekompose.core.middleware

import com.atwa.rekompose.store.AppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.reduxkotlin.middleware


fun coroutineDispatcherMiddleware(scope: CoroutineScope) = middleware<AppState> { store, next, action ->
    scope.launch {
        next(action)
    }
}