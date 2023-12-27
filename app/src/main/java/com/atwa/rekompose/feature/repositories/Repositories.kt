package com.atwa.rekompose.feature.repositories

import android.util.Log
import com.atwa.rekompose.core.di.ServiceLocator
import com.atwa.rekompose.core.effect.AffectedReducer
import com.atwa.rekompose.core.effect.affectedReducer
import com.atwa.rekompose.core.effect.withNoEffect
import com.atwa.rekompose.core.effect.withSuspendEffect
import com.atwa.rekompose.feature.filter.RepositoryLanguageFilter
import com.atwa.rekompose.store.Action

sealed interface RepositoriesAction : Action {
    object FetchRepositories : RepositoriesAction
    data class FetchRepositoriesSuccess(val repositories: List<Repository>) : RepositoriesAction
    data class FetchRepositoriesFailure(val error: String) : RepositoriesAction
    object FetchLanguageFilters : RepositoriesAction
    data class FetchLanguageFiltersSuccess(val languageFilters: MutableList<RepositoryLanguageFilter>) :
        RepositoriesAction

    data class UpdateFilterSelection(val id: Int, val isSelected: Boolean) : RepositoriesAction
}

data class RepositoriesState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val filters: List<RepositoryLanguageFilter> = listOf(),
    val selectedFilters: List<RepositoryLanguageFilter> = listOf(),
    val filteredRepositories: MutableList<Repository> = mutableListOf(),
    val repositories: List<Repository> = listOf(),
    val dialogLoading: Boolean = false,
)


val repositoriesReducer = affectedReducer<RepositoriesState, Action> { state, effect, action ->
    Log.d("THREAD NAME : ", "Reducer running on thread ${Thread.currentThread().name}")
    when (action) {
        is RepositoriesAction.FetchRepositories -> state.copy(isLoading = true).withSuspendEffect {
            ServiceLocator.githubRepo.fetchTrendingRepo()
        }

        is RepositoriesAction.FetchRepositoriesSuccess -> state.copy(
            isLoading = false,
            error = null,
            repositories = action.repositories,
            filteredRepositories = action.repositories.toMutableList()
        ).withNoEffect()

        is RepositoriesAction.FetchRepositoriesFailure -> state.copy(
            isLoading = false,
            error = action.error,
            repositories = listOf()
        ).withNoEffect()

        is RepositoriesAction.FetchLanguageFilters -> state.copy(dialogLoading = true)
            .withSuspendEffect {
                ServiceLocator.githubRepo.fetchLanguageFilters()
            }

        is RepositoriesAction.FetchLanguageFiltersSuccess -> state.copy(
            dialogLoading = false,
            filters = action.languageFilters
        ).withNoEffect()

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