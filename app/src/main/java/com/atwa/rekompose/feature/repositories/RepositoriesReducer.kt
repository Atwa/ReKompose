package com.atwa.rekompose.feature.repositories

import android.util.Log
import com.atwa.rekompose.feature.repositories.RepositoriesAction.*
import org.reduxkotlin.Reducer
import org.reduxkotlin.typedReducer

val repositoriesReducer: Reducer<RepositoriesState> =
    typedReducer<RepositoriesState, RepositoriesAction> { state, action ->
        Log.d("THREAD NAME : ","Reducer running on thread ${Thread.currentThread().name}")
        when (action) {
            is FetchingRepositories -> state.copy(isLoading = true)
            is FetchRepositoriesSuccess -> state.copy(
                isLoading = false,
                error = null,
                repositories = action.repositories,
                filteredRepositories = action.repositories.toMutableList()
            )
            is FetchRepositoriesFailure -> state.copy(
                isLoading = false,
                error = action.error,
                repositories = listOf()
            )
            is FetchingLanguageFilters -> state.copy(dialogLoading = true)
            is FetchLanguageFiltersSuccess -> state.copy(
                dialogLoading = false,
                filters = action.languageFilters
            )
            is UpdateFilterSelection -> {
                val updatedFilters =
                    state.filters.toMutableList().apply {
                        val index = indexOfFirst { it.id == action.id }
                        this[index] = this[index].copy(isSelected = action.isSelected)
                    }
                val selectedFilters = updatedFilters.filter { it.isSelected }
                if (selectedFilters.isEmpty())
                    return@typedReducer state.copy(
                        filteredRepositories = state.repositories.toMutableList(),
                        filters = updatedFilters,
                        selectedFilters = selectedFilters
                    )
                val filteredRepositories = state.repositories.groupBy { it.language }
                    .filterKeys { selectedFilters.map { filter -> filter.language }.contains(it) }
                    .flatMap { it.value }
                state.copy(
                    filteredRepositories = filteredRepositories.toMutableList(),
                    filters = updatedFilters,
                    selectedFilters = selectedFilters
                )
            }
            else -> state
        }
    }