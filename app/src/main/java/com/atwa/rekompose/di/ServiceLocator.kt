package com.atwa.rekompose.di

import com.atwa.rekompose.feature.repositories.GithubTrendingRepo
import com.atwa.rekompose.network.ApiClient
import kotlinx.coroutines.Dispatchers

object ServiceLocator {
    private val networkContext by lazy { Dispatchers.IO }
    val githubRepo by lazy { GithubTrendingRepo(networkContext, ApiClient) }
}

