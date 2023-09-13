package com.atwa.rekompose.feature.repositories

import com.atwa.rekompose.feature.filter.RepositoryLanguageFilter

sealed interface RepositoriesAction {
    object FetchRepositories : RepositoriesAction
    object FetchingRepositories : RepositoriesAction
    data class FetchRepositoriesSuccess(val repositories: List<Repository>) : RepositoriesAction
    data class FetchRepositoriesFailure(val error: String) : RepositoriesAction
    object FetchLanguageFilters : RepositoriesAction
    object FetchingLanguageFilters : RepositoriesAction
    data class FetchLanguageFiltersSuccess(val languageFilters: MutableList<RepositoryLanguageFilter>) : RepositoriesAction
    data class UpdateFilterSelection(val id: Int,val isSelected:Boolean) : RepositoriesAction
}