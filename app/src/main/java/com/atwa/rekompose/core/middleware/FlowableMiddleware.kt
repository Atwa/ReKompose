package com.atwa.rekompose.core.middleware

import com.atwa.rekompose.app.AppState
import com.atwa.rekompose.core.action.FlowableAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.launch
import org.reduxkotlin.middleware


fun flowableMiddleware(scope: CoroutineScope) = middleware<AppState> { store, next, action ->
    when (action) {
        is FlowableAction<*> -> {
            scope.launch(Dispatchers.Default) {
                action.run()
                    .flowOn(Dispatchers.Default)
                    .transformWhile { flowAction ->
                        emit(flowAction)
                        !flowAction.isComplete()
                    }.onStart {
                        if (action.isInitial()) next(action)
                    }.collect { flowAction ->
                        next(flowAction)
                    }
            }
        }

        else -> next(action)
    }
}
