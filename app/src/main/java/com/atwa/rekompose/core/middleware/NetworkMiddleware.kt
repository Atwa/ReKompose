package com.atwa.rekompose.core.middleware

import com.atwa.rekompose.feature.repositories.GithubTrendingRepo
import com.atwa.rekompose.feature.repositories.RepositoriesAction.FetchLanguageFilters
import com.atwa.rekompose.feature.repositories.RepositoriesAction.FetchRepositories
import com.atwa.rekompose.store.AppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.reduxkotlin.middleware


fun networkMiddleware(
    githubRepo: GithubTrendingRepo,
    scope: CoroutineScope,
) = middleware<AppState> { store, next, action ->
    var result = Any()
    scope.launch {
        val dispatch = store.dispatch
        result = next(action)
        when (action) {
            is FetchRepositories -> dispatch(githubRepo.fetchTrendingRepo())
            is FetchLanguageFilters -> dispatch(githubRepo.fetchLanguageFilters())
        }
    }
    result
}



