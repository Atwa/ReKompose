package com.atwa.rekompose.feature.repositories

import android.util.Log
import com.atwa.rekompose.core.network.ApiClient
import com.atwa.rekompose.feature.filter.RepositoryLanguageFilter
import kotlinx.coroutines.CoroutineScope

class GithubTrendingRepo(
    private val scope: CoroutineScope,
    private val apiClient: ApiClient,
) {

    suspend fun fetchTrendingRepo(query: String = "language") : RepositoriesAction {
        Log.d("THREAD NAME : ","Repo running on thread ${Thread.currentThread().name}")
        return apiClient.invokeCall<RepositoriesResponse>(
            FETCH_REPOS,
            hashMapOf(Pair("q", query))
        ).fold({ response ->
            RepositoriesAction.FetchRepositoriesSuccess(response.items.map { it.toDomain() })
        }, { error ->
            RepositoriesAction.FetchRepositoriesFailure(error.message ?: "Error")
        })
    }

    fun fetchLanguageFilters(): RepositoriesAction {
        Log.d("THREAD NAME : ","Repo running on thread ${Thread.currentThread().name}")
        val languageFilters = mutableListOf(
            RepositoryLanguageFilter(1, "Python"),
            RepositoryLanguageFilter(2, "C++"),
            RepositoryLanguageFilter(3, "Java"),
            RepositoryLanguageFilter(4, "Kotlin"),
            RepositoryLanguageFilter(5, "TypeScript"),
            RepositoryLanguageFilter(6, "Go"),
            RepositoryLanguageFilter(7, "JavaScript"),
            RepositoryLanguageFilter(8, "Julia")
        )
        return RepositoriesAction.FetchLanguageFiltersSuccess(languageFilters)
    }


    companion object {
        private const val FETCH_REPOS = "search/repositories"
    }

}