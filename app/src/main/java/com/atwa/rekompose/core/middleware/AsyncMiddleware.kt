package com.atwa.rekompose.core.middleware

import com.atwa.rekompose.app.AppState
import com.atwa.rekompose.core.action.AsyncAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.launch
import org.reduxkotlin.middleware


fun sideEffectMiddleware(scope: CoroutineScope) = middleware<AppState> { store, next, action ->
    scope.launch {
        when (action) {
            is AsyncAction -> action.run().flowOn(Dispatchers.IO).transformWhile { flowAction ->
                emit(flowAction)
                !flowAction.isComplete()
            }.collect { flowAction ->
                store.dispatch(flowAction)
            }
            else -> next(action)
        }
    }
}