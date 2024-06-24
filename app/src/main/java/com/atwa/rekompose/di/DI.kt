package com.atwa.rekompose.di

import com.atwa.rekompose.feature.repositories.data.RepositoriesTrendingRepo
import com.atwa.rekompose.core.network.ApiClient
import com.atwa.rekompose.core.threading.AppCoroutineScope

object DI {
    val coroutineScope by lazy { AppCoroutineScope }
    val apiClient by lazy { ApiClient("https://api.github.com/") }
    val githubRepo by lazy { RepositoriesTrendingRepo(apiClient) }
}

