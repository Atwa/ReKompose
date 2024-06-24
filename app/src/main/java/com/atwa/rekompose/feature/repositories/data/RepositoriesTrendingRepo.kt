package com.atwa.rekompose.feature.repositories.data

import com.atwa.rekompose.core.action.AsyncStatus.Success
import com.atwa.rekompose.core.action.SuspendAction
import com.atwa.rekompose.core.extensions.process
import com.atwa.rekompose.core.network.ApiClient
import com.atwa.rekompose.di.DI
import com.atwa.rekompose.feature.repositories.domain.LanguageFilter
import com.atwa.rekompose.feature.repositories.domain.RepositoriesAction.FetchFlowNumbers
import com.atwa.rekompose.feature.repositories.domain.RepositoriesAction.FetchLanguageFilters
import com.atwa.rekompose.feature.repositories.domain.RepositoriesAction.FetchRepositories
import com.atwa.rekompose.feature.repositories.domain.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class RepositoriesTrendingRepo(
    private val apiClient: ApiClient,
    private val scope: CoroutineScope = DI.coroutineScope,
) {

    private val numbersFlow = flow {
        emit(listOf(1, 2, 3, 4))
        emit(listOf(5, 6, 7, 8))
        emit(listOf(9, 10, 11, 12))
        emit(listOf(13, 14, 15, 16))
        emit(listOf(17, 18, 19, 20))
        emit(listOf(21, 22, 23, 24))
        emit(listOf(25, 26, 27, 28))
    }

    suspend fun fetchTrendingRepos(query: String = "language"): SuspendAction<List<Repository>> {
       return apiClient.invokeCall<RepositoriesResponse>(
            FETCH_REPOS,
            hashMapOf(Pair("q", query))
        ).process(FetchRepositories) { response ->
            response.items.map { it.toDomain() }
        }
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

    fun fetchFlowNumbers(): Flow<FetchFlowNumbers> {
        return numbersFlow.map { list ->
            FetchFlowNumbers(Success(list))
        }
    }

    companion object {
        private const val FETCH_REPOS = "search/repositories"
    }

}