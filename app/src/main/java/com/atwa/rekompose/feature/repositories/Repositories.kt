package com.atwa.rekompose.feature.repositories

import android.util.Log
import com.atwa.rekompose.core.di.ServiceLocator
import com.atwa.rekompose.core.effect.affectedReducer
import com.atwa.rekompose.core.effect.withNoEffect
import com.atwa.rekompose.core.middleware.AsyncAction
import com.atwa.rekompose.core.middleware.AsyncResult
import com.atwa.rekompose.feature.filter.LanguageFilter
import com.atwa.rekompose.store.Action

sealed interface RepositoriesAction : Action {
    object FetchRepositories : RepositoriesAction, AsyncAction<List<Repository>>({
        ServiceLocator.githubRepo.fetchTrendingRepo()
    })

    object FetchLanguageFilters : RepositoriesAction, AsyncAction<List<LanguageFilter>>({
        ServiceLocator.githubRepo.fetchLanguageFilters()
    })

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


val repositoriesReducer = affectedReducer<RepositoriesState, Action> { state, effect, action ->
    Log.d("THREAD NAME : ", "Reducer running on thread ${Thread.currentThread().name}")
    when (action) {
        is RepositoriesAction.FetchRepositories -> when (action.result) {
            is AsyncResult.Loading -> state.copy(isLoading = true).withNoEffect()
            is AsyncResult.Success -> state.copy(
                isLoading = false,
                error = null,
                repositories = action.data!!,
                filteredRepositories = action.result.data.toMutableList()
            ).withNoEffect()
            is AsyncResult.Failure -> state.copy(
                isLoading = false,
                error = action.error!!,
                repositories = listOf()
            ).withNoEffect()
        }

        is RepositoriesAction.FetchLanguageFilters -> when (action.result) {
            is AsyncResult.Loading -> state.copy(dialogLoading = true).withNoEffect()
            is AsyncResult.Failure -> state.withNoEffect()
            is AsyncResult.Success -> state.copy(
                dialogLoading = false,
                filters = action.result.data
            ).withNoEffect()
        }


        is RepositoriesAction.UpdateFilterSelection -> {
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
                ).withNoEffect()
            else {
                val filteredRepositories = state.repositories.groupBy { it.language }
                    .filterKeys { selectedFilters.map { filter -> filter.language }.contains(it) }
                    .flatMap { it.value }
                state.copy(
                    filteredRepositories = filteredRepositories.toMutableList(),
                    filters = updatedFilters,
                    selectedFilters = selectedFilters
                ).withNoEffect()
            }
        }

        else -> state.withNoEffect()
    }
    }