package com.atwa.rekompose.feature.repositories

import androidx.compose.runtime.Immutable
import com.atwa.rekompose.core.action.Action
import com.atwa.rekompose.core.action.AsyncAction
import com.atwa.rekompose.core.action.AsyncResult
import com.atwa.rekompose.core.action.AsyncStatus
import com.atwa.rekompose.di.DI
import com.atwa.rekompose.feature.filter.LanguageFilter
import com.atwa.rekompose.feature.repositories.RepositoriesAction.FetchLanguageFiltersResult
import com.atwa.rekompose.feature.repositories.RepositoriesAction.FetchRepositoriesResult
import com.atwa.rekompose.feature.repositories.RepositoriesAction.UpdateFilterSelection
import org.reduxkotlin.typedReducer

sealed interface RepositoriesAction : Action {
    object FetchRepositoriesAction : RepositoriesAction, AsyncAction {
        override fun run() = DI.githubRepo.fetchTrendingRepo()
    }

    object FetchLanguageFiltersAction : RepositoriesAction, AsyncAction {
        override fun run() = DI.githubRepo.fetchLanguageFilters()
    }

    object FetchRepositoriesResult : RepositoriesAction, AsyncResult<List<Repository>>()
    object FetchLanguageFiltersResult : RepositoriesAction, AsyncResult<List<LanguageFilter>>()
    data class UpdateFilterSelection(val id: Int, val isSelected: Boolean) : RepositoriesAction
}

@Immutable
data class RepositoriesState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val filters: List<LanguageFilter> = listOf(),
    val repositories: List<Repository> = listOf(),
    val dialogLoading: Boolean = false,
) {
    val filteredRepositories
        get() = if (selectedFilters.isEmpty()) repositories
        else repositories.groupBy { it.language }.filterKeys { key ->
            selectedFilters.map { filter -> filter.language }.contains(key)
        }.flatMap { map -> map.value }

    val selectedFilters get() = filters.filter { it.isSelected }
}

val repositoriesReducer = typedReducer<RepositoriesState, Action> { state, action ->
    when (action) {
        is FetchRepositoriesResult -> when (action.status) {
            is AsyncStatus.Loading -> state.copy(isLoading = true)
            is AsyncStatus.Success -> state.copy(
                isLoading = false,
                error = null,
                repositories = action.data
            )

            is AsyncStatus.Failure -> state.copy(
                isLoading = false,
                error = action.error,
                repositories = listOf()
            )
        }

        is FetchLanguageFiltersResult -> when (action.status) {
            is AsyncStatus.Loading -> state.copy(dialogLoading = true)
            is AsyncStatus.Failure -> state
            is AsyncStatus.Success -> state.copy(
                dialogLoading = false,
                filters = action.data
            )
        }

        is UpdateFilterSelection -> state.filters.toMutableList().run {
            val index = indexOfFirst { it.id == action.id }
            this[index] = this[index].copy(isSelected = action.isSelected)
            state.copy(filters = this)
        }

        else -> state
    }
}