package com.atwa.rekompose.core.network

import android.util.Log
import io.ktor.client.features.logging.*

object NetworkHttpLogger : Logger {
    private const val logTag = "NetworkHttpLogger"
    
    override fun log(message: String) {
        Log.d(logTag, message)
    }
}