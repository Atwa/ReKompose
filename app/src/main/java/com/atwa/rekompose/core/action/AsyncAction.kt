package com.atwa.rekompose.core.action

import kotlinx.coroutines.flow.Flow

fun interface AsyncAction : Action {
    fun run(): Flow<Action>
}