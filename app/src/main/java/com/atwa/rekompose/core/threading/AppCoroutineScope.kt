package com.atwa.rekompose.core.threading

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren

object AppCoroutineScope : CoroutineScope {
    override val coroutineContext = Dispatchers.Main + Job()
    fun cancel() {
        coroutineContext.cancelChildren()
        coroutineContext.cancel()
    }

}