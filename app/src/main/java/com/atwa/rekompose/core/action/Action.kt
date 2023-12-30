package com.atwa.rekompose.core.action

interface Action {
    fun isComplete() = false
    object INIT : Action
    object REPLACE : Action
}