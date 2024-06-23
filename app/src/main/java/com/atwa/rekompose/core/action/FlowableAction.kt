package com.atwa.rekompose.core.action

import kotlinx.coroutines.flow.Flow

abstract class FlowableAction <T : Any> : AsyncAction<T>() {

    override fun isComplete() = status !is AsyncStatus.Initial
    abstract fun run(): Flow<Action>


}
