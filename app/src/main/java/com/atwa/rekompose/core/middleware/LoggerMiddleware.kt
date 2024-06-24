package com.atwa.rekompose.core.middleware

import android.util.Log
import com.atwa.rekompose.app.AppState
import com.atwa.rekompose.core.action.Action
import kotlinx.coroutines.CoroutineScope
import org.reduxkotlin.middleware

fun loggerMiddleware(scope: CoroutineScope) = middleware<AppState> { store, next, action ->
    next(action)
    Log.v(
        "LOGGER - ****** :",
        "******************************************************************************************************************************"
    )
    if (action is Action) Log.d("LOGGER - ACTION : ", action.log())
    else Log.d("LOGGER - ACTION : ", action.toString())
    Log.d("LOGGER - STATE : ", "${store.state}")
    Log.v(
        "LOGGER - ****** :",
        "******************************************************************************************************************************"
    )
}