package com.atwa.rekompose.feature.repositories

import com.atwa.rekompose.core.action.Action
import com.atwa.rekompose.core.action.AsyncAction
import com.atwa.rekompose.di.DI
import com.atwa.rekompose.feature.filter.LanguageFilter

sealed interface RepositoriesAction : Action {
    data object FetchRepositoriesAsync : RepositoriesAction, AsyncAction<List<Repository>>() {
        override fun run() = DI.githubRepo.fetchTrendingRepo()
    }

    data object FetchLanguageFiltersAsync : RepositoriesAction,
        AsyncAction<List<LanguageFilter>>() {
        override fun run() = DI.githubRepo.fetchLanguageFilters()
    }

    data class UpdateFilterSelection(val id: Int, val isSelected: Boolean) : RepositoriesAction
}
