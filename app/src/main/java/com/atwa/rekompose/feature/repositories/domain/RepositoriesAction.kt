package com.atwa.rekompose.feature.repositories.domain

import com.atwa.rekompose.core.action.Action
import com.atwa.rekompose.core.action.AsyncStatus
import com.atwa.rekompose.core.action.FlowableAction
import com.atwa.rekompose.core.action.SuspendAction
import com.atwa.rekompose.di.DI.githubRepo
import kotlinx.coroutines.flow.Flow

sealed interface RepositoriesAction : Action {
    data object FetchRepositories : RepositoriesAction, SuspendAction<List<Repository>>() {
        override suspend fun run() = githubRepo.fetchTrendingRepos()
    }

    data object FetchLanguageFilters : RepositoriesAction, SuspendAction<List<LanguageFilter>>() {
        override suspend fun run() = githubRepo.fetchLanguageFilters()
    }

    class FetchFlowNumbers(
        override val status: AsyncStatus<List<Int>> = AsyncStatus.Initial,
        override val run: () -> Flow<Action> = githubRepo::fetchFlowNumbers,
    ) : RepositoriesAction, FlowableAction<List<Int>>(run, status)

    data class UpdateFilterSelection(val id: Int, val isSelected: Boolean) : RepositoriesAction
}
