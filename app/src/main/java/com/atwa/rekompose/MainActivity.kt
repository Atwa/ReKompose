package com.atwa.rekompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.atwa.rekompose.di.DI.coroutineScope
import com.atwa.rekompose.app.AppStore
import kotlinx.coroutines.job

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppStore()
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }


}