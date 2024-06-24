package com.atwa.rekompose.core.extensions

import com.atwa.rekompose.core.action.SuspendAction


fun <T : Any, R : Any> Result<T>.process(
    asyncAction: SuspendAction<R>,
    onSuccess: (value: T) -> R,
): SuspendAction<R> {
    return fold({ response ->
        asyncAction.success(onSuccess(response))
    }, { error ->
        asyncAction.failure(error)
    })
}
