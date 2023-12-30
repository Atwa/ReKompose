package com.atwa.rekompose.core.async

sealed interface AsyncResult<T : Any> {
    class Loading<T : Any> : AsyncResult<T>
    class Success<T : Any>(var data: T) : AsyncResult<T>
    class Failure<T : Any>(var error: String) : AsyncResult<T>
}