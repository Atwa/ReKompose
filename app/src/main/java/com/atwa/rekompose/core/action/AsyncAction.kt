package com.atwa.rekompose.core.action

import com.atwa.rekompose.core.action.AsyncStatus.Failure
import com.atwa.rekompose.core.action.AsyncStatus.Loading
import com.atwa.rekompose.core.action.AsyncStatus.Success


open class AsyncAction<T : Any> : Action {

    lateinit var status: AsyncStatus<T>

    override fun isComplete() = runCatching { status !is Loading }.getOrElse { false }
    val data
        get() = (status as? Success)?.data ?: Any() as T
    val error
        get() = (status as? Failure)?.error ?: ""

    fun loading() = apply {
        status = Loading()
    }

    fun success(content: T)  = apply {
        status = Success(content)
    }

    fun failure(exception: Throwable)  = apply {
        status = Failure(exception.message ?: "Error Occurred !")
    }

}
