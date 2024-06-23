package com.atwa.rekompose.core.extensions

import com.atwa.rekompose.core.action.Action
import com.atwa.rekompose.core.action.AsyncAction
import com.atwa.rekompose.core.action.SuspendAction


fun <T : Any, R : Any> Result<T>.process(
    asyncAction: AsyncAction<R>,
    onSuccess: (value: T) -> R,
): AsyncAction<R> {
    return fold({ response ->
        asyncAction.success(onSuccess(response))
    }, { error ->
        asyncAction.failure(error)
    })
}

fun <T : Any, R : Any> Result<T>.process(
    suspendAction: SuspendAction<R>,
    onSuccess: (value: T) -> R,
): AsyncAction<R> {
    return fold({ response ->
        suspendAction.success(onSuccess(response))
    }, { error ->
        suspendAction.failure(error)
    })
}
