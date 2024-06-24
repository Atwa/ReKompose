package com.atwa.rekompose.core.middleware

import com.atwa.rekompose.app.AppState
import com.atwa.rekompose.core.action.SuspendAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.reduxkotlin.middleware


fun suspendMiddleware(scope: CoroutineScope) = middleware<AppState> { store, next, action ->

    when (action) {
        is SuspendAction<*> -> {
            scope.launch(Dispatchers.Default) {
                if (action.isInitial()) {
                    next(action)
                    val resultAction = action.run()
                    next(resultAction)
                    resultAction.reset()
                }
            }
        }
        else -> next(action)
    }
}