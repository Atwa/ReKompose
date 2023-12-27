package com.atwa.rekompose.core.middleware

import android.util.Log
import com.atwa.rekompose.core.effect.affectedMiddleware
import com.atwa.rekompose.store.Action
import com.atwa.rekompose.store.AppState

fun loggerMiddleware() = affectedMiddleware<AppState,Action> { store, next, action ->
    val state = next(action)
    Log.d("THREAD NAME : ","Logger running on thread ${Thread.currentThread().name}")
    Log.d("******************", "******************************************************************************")
    Log.d("DISPATCHED ACTION : ", "${action::class.simpleName}: $action")
    Log.d("PRODUCED STATE : ", "${store.state}")
    Log.d("******************", "******************************************************************************")
    state
}