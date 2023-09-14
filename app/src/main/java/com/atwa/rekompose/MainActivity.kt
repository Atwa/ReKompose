package com.atwa.rekompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.atwa.rekompose.di.ServiceLocator.coroutineScope
import com.atwa.rekompose.store.AppStoreProvider
import kotlinx.coroutines.cancel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppStoreProvider()
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}