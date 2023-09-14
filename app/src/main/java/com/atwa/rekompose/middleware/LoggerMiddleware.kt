package com.atwa.rekompose.middleware

import android.util.Log
import com.atwa.rekompose.store.AppState
import org.reduxkotlin.middleware

fun loggerMiddleware() = middleware<AppState> { store, next, action ->
    val state = next(action)
    Log.d("THREAD NAME : ","Logger running on thread ${Thread.currentThread().name}")
    Log.d("******************", "******************************************************************************")
    Log.d("DISPATCHED ACTION : ", "${action::class.simpleName}: $action")
    Log.d("PRODUCED STATE : ", "${store.state}")
    Log.d("******************", "******************************************************************************")
    state
}