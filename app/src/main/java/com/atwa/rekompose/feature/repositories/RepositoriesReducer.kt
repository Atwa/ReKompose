package com.atwa.rekompose.feature.repositories

import com.atwa.rekompose.core.action.Action
import com.atwa.rekompose.core.action.AsyncStatus
import com.atwa.rekompose.feature.repositories.RepositoriesAction.FetchLanguageFiltersAsync
import com.atwa.rekompose.feature.repositories.RepositoriesAction.FetchRepositoriesAsync
import com.atwa.rekompose.feature.repositories.RepositoriesAction.UpdateFilterSelection
import org.reduxkotlin.typedReducer

val repositoriesReducer = typedReducer<RepositoriesState, Action> { state, action ->
    when (action) {
        is FetchRepositoriesAsync -> when (action.status) {
            is AsyncStatus.Initial -> state.copy(isLoading = true)
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
            is AsyncStatus.Initial -> state.copy(dialogLoading = true)
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