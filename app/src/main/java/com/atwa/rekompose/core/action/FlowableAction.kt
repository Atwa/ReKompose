package com.atwa.rekompose.core.action

import com.atwa.rekompose.core.action.AsyncStatus.Failure
import com.atwa.rekompose.core.action.AsyncStatus.Initial
import com.atwa.rekompose.core.action.AsyncStatus.Success
import kotlinx.coroutines.flow.Flow

open class FlowableAction<T : Any>(
    open val run: () -> Flow<Action>,
    open val status: AsyncStatus<T> = Initial,
) : Action {

    override fun log() = "${this::class.java.simpleName} - $status"
    override fun isComplete() = status is Failure
    fun isInitial() = status is Initial

    val data
        get() = (status as? Success)?.data
            ?: throw IllegalStateException("Data is accessed before it is set, make sure to check that status is success before accessing data field")
    val error
        get() = (status as? Failure)?.error ?: ""

}
