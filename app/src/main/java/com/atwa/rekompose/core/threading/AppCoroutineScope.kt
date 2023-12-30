package com.atwa.rekompose.core.threading

import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren

object AppCoroutineScope : CoroutineScope {
    override val coroutineContext = Dispatchers.Main + Job()

    fun cancel() {
        coroutineContext.cancel(CancellationException("App Destroyed"))
    }

}