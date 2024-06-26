# ReKompose
Android Sample app with central state management built upon [Redux-Kotlin](https://github.com/reduxkotlin/redux-kotlin) & [Redux-Kotlin-Compose](https://github.com/reduxkotlin/redux-kotlin-compose).

**ReKompose stands for : (Re -> Redux) + (Ko -> Kotlin) + (Kompose -> Compose).**

##  Tech stack:

**Kotlin, Redux-Kotlin, Redux-Kotlin-Compose , Jetpack Compose, Ktor, Coroutines, Service Locator(DI).<br />
The app can be migrated to be a multiplatform project easily since it's pure kotlin and UI is pure compose.**

##  Source Code representation :

- feature : Contains sub packages which represent the feature screens of the app.
  - repositories : Repositories list screen showing list of Github trending repos and chips for selected language filters.
  - filter : Repositories language filters list enabling user to select/unselect language filters.
- middleware : Middlewares that interrupts actions dispatched and can cause side effects if needed.
  - coroutineDispatcherMiddleware : Executes Reducers and middlewares logic in a background thread. 
  - NetworkMiddleware : Designed as a thunk middleware that call repository functions responsible for calling api endpoints. 
  - LoggerMiddleware : Logging each dispatched action and it's corresponding produced state.
- network : Network client & logger configurations.
  - ApiClient : Singleton responsible for configuring Ktor client and invoking api calls through the configured client.
  - NetworkHttpLogger : Logger that logs network requests going through Ktor client.
- threading : Achieving synchronization & Enhancing performance by using background thread.
  - AppCoroutineScope : A coroutine scope using single thread context to synchronize store state. <br />
    Improves performance by moving implementation into background thread aside from main thread. 
- store : Provides app store with root reducer and initial app state.
- ui : Contains all ui related stuff throughout the app such as reusable components, themes and colors.
- di : Dependency injection using Service locator design pattern.

**Please note that each developer has his own style in coding ,The goal is to implement the concepts of the design and architecting in the right way .**


| Loading| Repositories | Filters |
|:-----:|:--------:|:--------:|
| <img src="loading_shimmer.png" width="200" hspace="2" alt="loading" /> | <img src="repositories.png" width="200" hspace="2" alt="repositories" /> |<img src="filters_list.png" width="200" hspace="2" alt="filters list" /> |

| Selected Dialog Filter | Filtered Repositories | Error |
|:-----:|:--------:|:--------:|
| <img src="filters_dialog_selected.png" width="200" hspace="2" alt="filters dialog" /> | <img src="repositories_list_with_filter.png" width="200" hspace="2" alt="filtered" /> |<img src="error_screen.png" width="200" hspace="2" alt="error" /> |

### Contribution

Please feel free to make a pull request or fork.

### Rate

If you find this repository useful please give it a star .
