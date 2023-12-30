package com.atwa.rekompose.core.async

import com.atwa.rekompose.app.Action
import kotlinx.coroutines.flow.Flow

fun interface AsyncAction : Action {
    fun run(): Flow<Action>
}