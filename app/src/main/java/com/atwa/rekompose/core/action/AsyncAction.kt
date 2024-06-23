package com.atwa.rekompose.core.action

import com.atwa.rekompose.core.action.AsyncStatus.Failure
import com.atwa.rekompose.core.action.AsyncStatus.Initial
import com.atwa.rekompose.core.action.AsyncStatus.Success
import kotlinx.coroutines.flow.Flow


abstract class AsyncAction<T : Any> : Action {

    var status: AsyncStatus<T> = Initial()

    fun log() = "${toString()} - $status"
    fun isInitial() = status is Initial

    val data
        get() = (status as? Success)?.data
            ?: throw IllegalStateException("Data is accessed before it is set, make sure to check that status is success before accessing data field")
    val error
        get() = (status as? Failure)?.error ?: ""

    fun reset() = apply {
        status = Initial()
    }

    fun success(content: T) = apply {
        status = Success(content)
    }

    fun failure(exception: Throwable) = apply {
        status = Failure(exception.message ?: "Error Occurred !")
    }
}
