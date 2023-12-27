package com.atwa.rekompose.core.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import org.reduxkotlin.Dispatcher
import org.reduxkotlin.TypedDispatcher
import org.reduxkotlin.compose.StoreProvider
import org.reduxkotlin.compose.rememberDispatcher
import org.reduxkotlin.compose.rememberTypedDispatcher
import org.reduxkotlin.compose.rememberTypedStore


@Suppress("PrivatePropertyName")
private val LocalStore: ProvidableCompositionLocal<AffectedStore<*, *>> =
    compositionLocalOf { error("undefined") }

/**
 * Retrieves a [Store] from the current composition scope
 * @param State the type of the state the store is expected to hold
 * @return Retrieved [Store]
 * @see StoreProvider
 * @see rememberTypedStore
 */
@Composable
@Suppress("UNCHECKED_CAST")
public fun <State,Action> rememberStore(): AffectedStore<State,Action> = LocalStore.current as AffectedStore<State,Action>

/**
 * Retrieves a [TypedStore] from the current composition scope
 * @param State the type of the state the store is expected to hold
 * @param State type
 * @param Action type
 * @return Retrieved [TypedStore]
 * @see StoreProvider
 * @see rememberStore
 */
@Composable
@Suppress("UNCHECKED_CAST")
public fun <State, Action> rememberAffectedStore(): AffectedStore<State, Action> =
    LocalStore.current as AffectedStore<State, Action>


@Composable
public fun <State, Action, Store : AffectedStore<State, Action>> AffectedStoreProvider(
    store: Store,
    content: @Composable Store.() -> Unit
) {
    CompositionLocalProvider(LocalStore provides store) {
        store.content()
    }
}

/**
 * Retrieves a [Dispatcher] from the current local store
 * @return retrieved [Dispatcher]
 * @see StoreProvider
 * @see rememberDispatcher
 */
@Composable
public fun <Action> rememberAffectedDispatcher(): TypedDispatcher<Action> =
    rememberAffectedStore<Any, Action>().dispatch