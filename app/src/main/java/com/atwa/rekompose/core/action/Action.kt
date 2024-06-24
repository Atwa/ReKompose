package com.atwa.rekompose.core.action

interface Action {
    fun isComplete() = false
    fun log() = toString()
}