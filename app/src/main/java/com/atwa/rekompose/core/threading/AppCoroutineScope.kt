package com.atwa.rekompose.core.threading

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext

object AppCoroutineScope : CoroutineScope {
    @OptIn(DelicateCoroutinesApi::class)
    override val coroutineContext = newSingleThreadContext("Redux-Thread")

    fun cancel() = coroutineContext.close()

}