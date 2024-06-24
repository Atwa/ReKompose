package com.atwa.rekompose.core.action

sealed interface AsyncStatus<out T : Any> {
    data object Initial : AsyncStatus<Nothing>
    data class Success<T : Any>(var data: T) : AsyncStatus<T>
    data class Failure<T : Any>(var error: String = "Error occurred!") : AsyncStatus<T>
}