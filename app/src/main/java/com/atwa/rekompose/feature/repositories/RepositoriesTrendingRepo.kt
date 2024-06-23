package com.atwa.rekompose.feature.repositories

import com.atwa.rekompose.core.extensions.process
import com.atwa.rekompose.core.network.ApiClient
import com.atwa.rekompose.di.DI
import com.atwa.rekompose.feature.filter.LanguageFilter
import com.atwa.rekompose.feature.repositories.RepositoriesAction.FetchLanguageFilters
import com.atwa.rekompose.feature.repositories.RepositoriesAction.FetchRepositories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow

class RepositoriesTrendingRepo(
    private val apiClient: ApiClient,
    private val scope: CoroutineScope = DI.coroutineScope,
) {

    fun fetchTrendingRepo(query: String = "language") = flow {
        apiClient.invokeCall<RepositoriesResponse>(
            FETCH_REPOS,
            hashMapOf(Pair("q", query))
        ).process(FetchRepositories) { response ->
            response.items.map { it.toDomain() }
        }.also { emit(it) }
    }

    suspend fun fetchTrendingRepos(query: String = "language") =
        apiClient.invokeCall<RepositoriesResponse>(
            FETCH_REPOS,
            hashMapOf(Pair("q", query))
        ).process(FetchRepositories) { response ->
            response.items.map { it.toDomain() }
        }


    suspend fun fetchLanguageFilters() = mutableListOf(
        LanguageFilter(1, "Python"),
        LanguageFilter(2, "C++"),
        LanguageFilter(3, "Java"),
        LanguageFilter(4, "Kotlin"),
        LanguageFilter(5, "TypeScript"),
        LanguageFilter(6, "Go"),
        LanguageFilter(7, "JavaScript"),
        LanguageFilter(8, "Julia")
    ).let { filters ->
        FetchLanguageFilters.success(filters)
    }


    companion object {
        private const val FETCH_REPOS = "search/repositories"
    }

}