package com.atwa.rekompose.core.action

import kotlinx.coroutines.flow.Flow

fun interface SideEffect : Action {
    fun run(): Flow<Action>
}