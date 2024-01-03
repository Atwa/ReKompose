package com.atwa.rekompose.feature.repositories.presentation

import android.util.Log
import androidx.compose.runtime.Immutable
import com.atwa.rekompose.core.action.Action
import com.atwa.rekompose.core.action.AsyncAction
import com.atwa.rekompose.core.action.AsyncStatus
import com.atwa.rekompose.core.action.SideEffect
import com.atwa.rekompose.core.di.ServiceLocator
import com.atwa.rekompose.feature.filter.domain.LanguageFilter
import com.atwa.rekompose.feature.repositories.domain.Repository
import com.atwa.rekompose.feature.repositories.presentation.RepositoriesAction.FetchLanguageFiltersAsync
import com.atwa.rekompose.feature.repositories.presentation.RepositoriesAction.FetchRepositoriesAsync
import com.atwa.rekompose.feature.repositories.presentation.RepositoriesAction.UpdateFilterSelection
import org.reduxkotlin.typedReducer

sealed interface RepositoriesAction : Action {
    object FetchRepositoriesEffect : RepositoriesAction, SideEffect {
        override fun run() = ServiceLocator.githubRepo.fetchTrendingRepo()
    }

    object FetchLanguageFiltersEffect : RepositoriesAction, SideEffect {
        override fun run() = ServiceLocator.githubRepo.fetchLanguageFilters()
    }

    object FetchRepositoriesAsync : RepositoriesAction, AsyncAction<List<Repository>>()
    object FetchLanguageFiltersAsync : RepositoriesAction, AsyncAction<List<LanguageFilter>>()
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
        is FetchRepositoriesAsync -> when (action.status) {
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

        is FetchLanguageFiltersAsync -> when (action.status) {
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