package com.atwa.rekompose.core.middleware

import android.util.Log
import com.atwa.rekompose.app.AppState
import org.reduxkotlin.middleware

fun loggerMiddleware() = middleware<AppState> { store, next, action ->
    val state = next(action)
    Log.d("THREAD NAME : ","Logger running on thread ${Thread.currentThread().name}")
    Log.d("******************", "******************************************************************************")
    Log.d("DISPATCHED ACTION : ", action.toString())
    Log.d("PRODUCED STATE : ", "${store.state}")
    Log.d("******************", "******************************************************************************")
    state
}