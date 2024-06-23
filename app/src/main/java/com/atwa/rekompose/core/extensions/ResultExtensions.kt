package com.atwa.rekompose.core.extensions

import com.atwa.rekompose.core.action.AsyncAction


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
