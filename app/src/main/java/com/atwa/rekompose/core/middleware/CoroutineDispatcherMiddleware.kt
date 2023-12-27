package com.atwa.rekompose.core.middleware

import com.atwa.rekompose.core.effect.affectedMiddleware
import com.atwa.rekompose.store.Action
import com.atwa.rekompose.store.AppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun coroutineDispatcherMiddleware(scope: CoroutineScope) = affectedMiddleware<AppState,Action> { store, next, action ->
    scope.launch {
        next(action)
    }
}