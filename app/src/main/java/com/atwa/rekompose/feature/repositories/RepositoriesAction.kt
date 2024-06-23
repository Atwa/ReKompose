package com.atwa.rekompose.feature.repositories

import com.atwa.rekompose.core.action.Action
import com.atwa.rekompose.core.action.SuspendAction
import com.atwa.rekompose.di.DI.githubRepo
import com.atwa.rekompose.feature.filter.LanguageFilter

sealed interface RepositoriesAction : Action {
    data object FetchRepositories : RepositoriesAction, SuspendAction<List<Repository>>() {
        override suspend fun run() = githubRepo.fetchTrendingRepos()
    }

    data object FetchLanguageFilters : RepositoriesAction, SuspendAction<List<LanguageFilter>>() {
        override suspend fun run() = githubRepo.fetchLanguageFilters()
    }

    data class UpdateFilterSelection(val id: Int, val isSelected: Boolean) : RepositoriesAction
}
