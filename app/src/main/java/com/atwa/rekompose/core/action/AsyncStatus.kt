package com.atwa.rekompose.core.action

sealed interface AsyncStatus<T : Any> {

    data class Initial<T : Any>(val mock: Any? = null) : AsyncStatus<T> {
        override fun toString(): String = "Initial"
    }
    data class Success<T : Any>(var data: T) : AsyncStatus<T>
    data class Failure<T : Any>(var error: String) : AsyncStatus<T>
}