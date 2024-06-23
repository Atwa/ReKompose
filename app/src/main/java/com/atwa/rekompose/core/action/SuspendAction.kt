package com.atwa.rekompose.core.action

abstract class SuspendAction<T : Any> : AsyncAction<T>() {

    override fun isComplete() = status !is AsyncStatus.Initial
    abstract suspend fun run(): AsyncAction<T>

}