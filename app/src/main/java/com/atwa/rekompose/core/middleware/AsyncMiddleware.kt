package com.atwa.rekompose.core.middleware

import com.atwa.rekompose.core.effect.affectedMiddleware
import com.atwa.rekompose.store.Action
import com.atwa.rekompose.store.AppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

open class AsyncAction<T : Any>(
    val actions: () -> Flow<Action>,
    val result: AsyncResult<T> = AsyncResult.Loading(),
) : Action {

    override fun isComplete() = result !is AsyncResult.Loading
    val data
        get() = if (result is AsyncResult.Success) result.data else null
    val error
        get() = if (result is AsyncResult.Failure) result.error else null

    fun loading() = AsyncAction<T>(
        actions = actions,
        result = AsyncResult.Loading()
    )

    fun success(content: T) = AsyncAction(
        actions = actions,
        result = AsyncResult.Success(content)
    )

    fun failure(exception: Throwable) = AsyncAction<T>(
        actions = actions,
        result = AsyncResult.Failure(exception.message ?: "Error Occurred !")
    )

}

sealed class AsyncResult<T : Any> {
    class Loading<T : Any> : AsyncResult<T>()
    class Success<T : Any>(var data: T) : AsyncResult<T>()
    class Failure<T : Any>(var error: String) : AsyncResult<T>()
}

fun asyncMiddleware(scope: CoroutineScope, timeout: Long) =
    affectedMiddleware<AppState, Action> { store, next, action ->
        when (action) {
            is AsyncAction<*> ->
                scope.launch {
                    action.actions().flowOn(Dispatchers.IO)
                        .collect { asyncAction ->
                            if (asyncAction.isComplete()) next(asyncAction)
                            else store.dispatch(asyncAction)
                        }
                }

            else -> next(action)
        }
    }