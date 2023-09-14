package com.atwa.rekompose.feature.repositories

import android.util.Log
import com.atwa.rekompose.feature.filter.RepositoryLanguageFilter
import com.atwa.rekompose.network.ApiClient
import com.atwa.rekompose.store.AppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.reduxkotlin.thunk.Thunk
import kotlin.coroutines.CoroutineContext

class GithubTrendingRepo(
    private val scope: CoroutineScope,
    private val apiClient: ApiClient,
)  {


    suspend fun fetchTrendingRepo(query: String = "language"): Thunk<AppState> =
        { dispatch, getState, extraArg ->
            scope.launch {
                Log.d("THREAD NAME : ","Repository running on thread ${Thread.currentThread().name}")
                dispatch(RepositoriesAction.FetchingRepositories)
                val result = apiClient.invokeCall<RepositoriesResponse>(
                    FETCH_REPOS,
                    hashMapOf(Pair("q", query))
                )
                result.fold({ response ->
                    dispatch(RepositoriesAction.FetchRepositoriesSuccess(response.items.map { it.toDomain() }))
                }, { error ->
                    dispatch(RepositoriesAction.FetchRepositoriesFailure(error.message ?: "Error"))
                })
            }
        }

    fun fetchLanguageFilters(): Thunk<AppState> =
        { dispatch, getState, extraArg ->
            scope.launch(Dispatchers.IO) {
                dispatch(RepositoriesAction.FetchingLanguageFilters)
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
                dispatch(RepositoriesAction.FetchLanguageFiltersSuccess(languageFilters))
            }
        }


    companion object {
        private const val FETCH_REPOS = "search/repositories"
    }

}