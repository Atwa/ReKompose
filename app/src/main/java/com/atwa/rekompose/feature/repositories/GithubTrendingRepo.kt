package com.atwa.rekompose.feature.repositories

import com.atwa.rekompose.core.network.ApiClient
import com.atwa.rekompose.feature.filter.LanguageFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow

class GithubTrendingRepo(
    private val scope: CoroutineScope,
    private val apiClient: ApiClient,
) {

    fun fetchTrendingRepo(query: String = "language") = flow {
        emit(RepositoriesAction.FetchRepositories.loading())
        apiClient.invokeCall<RepositoriesResponse>(
            FETCH_REPOS,
            hashMapOf(Pair("q", query))
        ).fold({ response ->
            RepositoriesAction.FetchRepositories.success(response.items.map { it.toDomain() })
        }, { error ->
            RepositoriesAction.FetchRepositories.failure(error)
        }).let { emit(it) }
    }

    fun fetchLanguageFilters() = flow {
        mutableListOf(
            LanguageFilter(1, "Python"),
            LanguageFilter(2, "C++"),
            LanguageFilter(3, "Java"),
            LanguageFilter(4, "Kotlin"),
            LanguageFilter(5, "TypeScript"),
            LanguageFilter(6, "Go"),
            LanguageFilter(7, "JavaScript"),
            LanguageFilter(8, "Julia")
        ).let { filters ->
            emit(RepositoriesAction.FetchLanguageFilters.success(filters))
        }
    }


    companion object {
        private const val FETCH_REPOS = "search/repositories"
    }

}