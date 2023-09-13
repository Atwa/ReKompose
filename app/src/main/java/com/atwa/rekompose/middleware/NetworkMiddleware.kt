package com.atwa.rekompose.middleware

import com.atwa.rekompose.feature.repositories.GithubTrendingRepo
import com.atwa.rekompose.feature.repositories.RepositoriesAction.FetchLanguageFilters
import com.atwa.rekompose.feature.repositories.RepositoriesAction.FetchRepositories
import com.atwa.rekompose.store.AppState
import org.reduxkotlin.middleware


fun networkMiddleware(githubRepo: GithubTrendingRepo) = middleware<AppState> { store, next, action ->
    val dispatch = store.dispatch
    val result = next(action)
    when (action) {
        is FetchRepositories -> dispatch(githubRepo.fetchTrendingRepo())
        is FetchLanguageFilters -> dispatch(githubRepo.fetchLanguageFilters())
    }
    result
}



