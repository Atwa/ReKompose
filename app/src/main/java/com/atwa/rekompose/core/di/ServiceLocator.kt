package com.atwa.rekompose.core.di

import com.atwa.rekompose.feature.repositories.GithubTrendingRepo
import com.atwa.rekompose.core.network.ApiClient
import com.atwa.rekompose.core.threading.AppCoroutineScope
import kotlinx.coroutines.*

object ServiceLocator {
    @OptIn(DelicateCoroutinesApi::class)
    val coroutineScope by lazy { AppCoroutineScope }
    val githubRepo by lazy { GithubTrendingRepo(coroutineScope, ApiClient) }
}

