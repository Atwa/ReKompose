package com.atwa.rekompose.core.middleware

import android.util.Log
import com.atwa.rekompose.app.AppState
import com.atwa.rekompose.core.action.Action
import com.atwa.rekompose.core.action.AsyncAction
import org.reduxkotlin.middleware

fun loggerMiddleware() = middleware<AppState> { store, next, action ->
    next(action)
    Log.v("LOGGER - ****** :", "******************************************************************************************************************************")
    if (action is AsyncAction<*>) Log.d("LOGGER - ACTION : ", action.log())
    else Log.d("LOGGER - ACTION : ", action.toString())
    Log.d("LOGGER - STATE : ", "${store.state}")
    Log.v("LOGGER - ****** :", "******************************************************************************************************************************")
}