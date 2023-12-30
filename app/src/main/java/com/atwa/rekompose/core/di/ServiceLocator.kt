package com.atwa.rekompose.core.di

import com.atwa.rekompose.feature.repositories.data.GithubTrendingRepo
import com.atwa.rekompose.core.network.ApiClient
import com.atwa.rekompose.core.threading.AppCoroutineScope
import kotlinx.coroutines.*

object ServiceLocator {
    val coroutineScope by lazy { AppCoroutineScope }
    val githubRepo by lazy { GithubTrendingRepo(ApiClient) }
}

