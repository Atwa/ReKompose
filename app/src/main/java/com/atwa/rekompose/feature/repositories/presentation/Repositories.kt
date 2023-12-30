package com.atwa.rekompose.feature.repositories.presentation

import android.util.Log
import com.atwa.rekompose.app.Action
import com.atwa.rekompose.core.async.AsyncAction
import com.atwa.rekompose.core.async.AsyncResult
import com.atwa.rekompose.core.async.ResultAction
import com.atwa.rekompose.core.di.ServiceLocator
import com.atwa.rekompose.feature.filter.domain.LanguageFilter
import com.atwa.rekompose.feature.repositories.domain.Repository
import com.atwa.rekompose.feature.repositories.presentation.RepositoriesAction.FetchLanguageFiltersResult
import com.atwa.rekompose.feature.repositories.presentation.RepositoriesAction.FetchRepositoriesResult
import com.atwa.rekompose.feature.repositories.presentation.RepositoriesAction.UpdateFilterSelection
import org.reduxkotlin.typedReducer

sealed interface RepositoriesAction : Action {
    object FetchRepositories : RepositoriesAction, AsyncAction {
        override fun run() = ServiceLocator.githubRepo.fetchTrendingRepo()
    }

    object FetchLanguageFilters : RepositoriesAction, AsyncAction {
        override fun run() = ServiceLocator.githubRepo.fetchLanguageFilters()
    }

    object FetchRepositoriesResult : RepositoriesAction, ResultAction<List<Repository>>()
    object FetchLanguageFiltersResult : RepositoriesAction, ResultAction<List<LanguageFilter>>()
    data class UpdateFilterSelection(val id: Int, val isSelected: Boolean) : RepositoriesAction
}

data class RepositoriesState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val filters: List<LanguageFilter> = listOf(),
    val selectedFilters: List<LanguageFilter> = listOf(),
    val filteredRepositories: MutableList<Repository> = mutableListOf(),
    val repositories: List<Repository> = listOf(),
    val dialogLoading: Boolean = false,
)

val repositoriesReducer = typedReducer<RepositoriesState, Action> { state, action ->
    Log.d("THREAD NAME : ", "Reducer running on thread ${Thread.currentThread().name}")
    when (action) {
        is FetchRepositoriesResult -> when (action.result) {
            is AsyncResult.Loading -> state.copy(isLoading = true)
            is AsyncResult.Success -> state.copy(
                isLoading = false,
                error = null,
                repositories = action.data,
                filteredRepositories = action.data.toMutableList()
            )

            is AsyncResult.Failure -> state.copy(
                isLoading = false,
                error = action.error,
                repositories = listOf()
            )
        }

        is FetchLanguageFiltersResult -> when (action.result) {
            is AsyncResult.Loading -> state.copy(dialogLoading = true)
            is AsyncResult.Failure -> state
            is AsyncResult.Success -> state.copy(
                dialogLoading = false,
                filters = action.data
            )
        }


        is UpdateFilterSelection -> {
            val updatedFilters =
                state.filters.toMutableList().apply {
                    val index = indexOfFirst { it.id == action.id }
                    this[index] = this[index].copy(isSelected = action.isSelected)
                }
            val selectedFilters = updatedFilters.filter { it.isSelected }
            if (selectedFilters.isEmpty())
                state.copy(
                    filteredRepositories = state.repositories.toMutableList(),
                    filters = updatedFilters,
                    selectedFilters = selectedFilters
                )
            else {
                val filteredRepositories = state.repositories.groupBy { it.language }
                    .filterKeys { selectedFilters.map { filter -> filter.language }.contains(it) }
                    .flatMap { it.value }
                state.copy(
                    filteredRepositories = filteredRepositories.toMutableList(),
                    filters = updatedFilters,
                    selectedFilters = selectedFilters
                )
            }
        }

        else -> state
    }
}