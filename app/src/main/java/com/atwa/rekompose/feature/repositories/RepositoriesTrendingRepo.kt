package com.atwa.rekompose.feature.repositories

import com.atwa.rekompose.core.action.Action
import com.atwa.rekompose.core.network.ApiClient
import com.atwa.rekompose.feature.filter.LanguageFilter
import com.atwa.rekompose.feature.repositories.RepositoriesAction.FetchLanguageFiltersResult
import com.atwa.rekompose.feature.repositories.RepositoriesAction.FetchRepositoriesResult
import kotlinx.coroutines.flow.flow

class RepositoriesTrendingRepo(
    private val apiClient: ApiClient,
) {

    fun fetchTrendingRepo(query: String = "language") = flow<Action> {
        emit(FetchRepositoriesResult.loading())
        apiClient.invokeCall<RepositoriesResponse>(
            FETCH_REPOS,
            hashMapOf(Pair("q", query))
        ).fold({ response ->
            FetchRepositoriesResult.success(response.items.map { it.toDomain() })
        }, { error ->
            FetchRepositoriesResult.failure(error)
        }).let { emit(it) }
    }

    fun fetchLanguageFilters() = flow<Action> {
        emit(FetchLanguageFiltersResult.loading())
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
            emit(FetchLanguageFiltersResult.success(filters))
        }
    }


    companion object {
        private const val FETCH_REPOS = "search/repositories"
    }

}