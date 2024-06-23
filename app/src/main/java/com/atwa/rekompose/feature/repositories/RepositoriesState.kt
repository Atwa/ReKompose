package com.atwa.rekompose.feature.repositories

import androidx.compose.runtime.Immutable
import com.atwa.rekompose.core.action.Action
import com.atwa.rekompose.core.action.AsyncAction
import com.atwa.rekompose.di.DI
import com.atwa.rekompose.feature.filter.LanguageFilter


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