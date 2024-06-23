package com.atwa.rekompose.feature.repositories

import com.atwa.rekompose.core.action.Action
import com.atwa.rekompose.core.extensions.process
import com.atwa.rekompose.core.network.ApiClient
import com.atwa.rekompose.feature.filter.LanguageFilter
import com.atwa.rekompose.feature.repositories.RepositoriesAction.FetchLanguageFiltersAsync
import com.atwa.rekompose.feature.repositories.RepositoriesAction.FetchRepositoriesAsync
import kotlinx.coroutines.flow.flow

class RepositoriesTrendingRepo(
    private val apiClient: ApiClient,
) {

    fun fetchTrendingRepo(query: String = "language") = flow<Action> {
        apiClient.invokeCall<RepositoriesResponse>(
            FETCH_REPOS,
            hashMapOf(Pair("q", query))
        ).process(FetchRepositoriesAsync) { response ->
            response.items.map { it.toDomain() }
        }.also { emit(it) }
    }

    fun fetchLanguageFilters() = flow<Action> {
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
            emit(FetchLanguageFiltersAsync.success(filters))
        }
    }


    companion object {
        private const val FETCH_REPOS = "search/repositories"
    }

}