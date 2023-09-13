package com.atwa.rekompose.feature.repositories

import com.atwa.rekompose.feature.filter.RepositoryLanguageFilter

data class RepositoriesState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val filters: List<RepositoryLanguageFilter> = listOf(),
    val selectedFilters: List<RepositoryLanguageFilter> = listOf(),
    val filteredRepositories: MutableList<Repository> = mutableListOf(),
    val repositories: List<Repository> = listOf(),
    val dialogLoading: Boolean = false
)

