package com.atwa.rekompose.core.async

import com.atwa.rekompose.app.Action
import com.atwa.rekompose.core.async.AsyncResult.Failure
import com.atwa.rekompose.core.async.AsyncResult.Loading
import com.atwa.rekompose.core.async.AsyncResult.Success


open class ResultAction<T : Any> : Action {

    lateinit var result: AsyncResult<T>

    fun isComplete() = runCatching { result !is Loading }.getOrElse { false }
    val data
        get() = (result as? Success)?.data ?: Any() as T
    val error
        get() = (result as? Failure)?.error ?: ""

    fun loading() = apply {
        result = Loading()
    }

    fun success(content: T)  = apply {
        result = Success(content)
    }

    fun failure(exception: Throwable)  = apply {
        result = Failure(exception.message ?: "Error Occurred !")
    }

}
