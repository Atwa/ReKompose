package com.atwa.rekompose.core.action

abstract class SuspendAction<T : Any> : Action {


    var status: AsyncStatus<T> = AsyncStatus.Initial

    override fun log() = "${toString()} - $status"
    override fun isComplete() = status !is AsyncStatus.Initial
    fun isInitial() = status is AsyncStatus.Initial
    abstract suspend fun run(): SuspendAction<T>

    val data
        get() = (status as? AsyncStatus.Success)?.data
            ?: throw IllegalStateException("Data is accessed before it is set, make sure to check that status is success before accessing data field")
    val error
        get() = (status as? AsyncStatus.Failure)?.error ?: ""

    fun reset() = apply {
        status = AsyncStatus.Initial
    }

    fun success(content: T) = apply {
        status = AsyncStatus.Success(content)
    }

    fun failure(exception: Throwable) = apply {
        status = AsyncStatus.Failure(exception.message ?: "Error Occurred !")
    }

}