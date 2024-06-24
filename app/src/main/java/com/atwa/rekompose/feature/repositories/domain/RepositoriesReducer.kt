package com.atwa.rekompose.feature.repositories.domain

import android.util.Log
import com.atwa.rekompose.core.action.Action
import com.atwa.rekompose.core.action.AsyncStatus.Failure
import com.atwa.rekompose.core.action.AsyncStatus.Initial
import com.atwa.rekompose.core.action.AsyncStatus.Success
import com.atwa.rekompose.feature.repositories.presentation.RepositoriesState
import com.atwa.rekompose.feature.repositories.domain.RepositoriesAction.FetchFlowNumbers
import com.atwa.rekompose.feature.repositories.domain.RepositoriesAction.FetchLanguageFilters
import com.atwa.rekompose.feature.repositories.domain.RepositoriesAction.FetchRepositories
import com.atwa.rekompose.feature.repositories.domain.RepositoriesAction.UpdateFilterSelection
import org.reduxkotlin.typedReducer

val repositoriesReducer = typedReducer<RepositoriesState, Action> { state, action ->
    when (action) {
        is FetchRepositories -> when (action.status) {
            is Initial -> state.copy(isLoading = true)
            is Success -> state.copy(
                isLoading = false,
                error = null,
                repositories = action.data
            )

            is Failure -> state.copy(
                isLoading = false,
                error = action.error,
                repositories = listOf()
            )
        }

        is FetchLanguageFilters -> when (action.status) {
            is Initial -> state.copy(dialogLoading = true)
            is Failure -> state
            is Success -> state.copy(
                dialogLoading = false,
                filters = action.data
            )
        }

        is FetchFlowNumbers -> {
            Log.v("FlowAction", "Flow status is ${action.status}")
            state
        }

        is UpdateFilterSelection -> state.filters.toMutableList().run {
            val index = indexOfFirst { it.id == action.id }
            this[index] = this[index].copy(isSelected = action.isSelected)
            state.copy(filters = this)
        }

        else -> state
    }
}