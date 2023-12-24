package com.atwa.rekompose.core.effect

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

@OptIn(FlowPreview::class)
class Effect<Output>(internal var flow: Flow<Output>) {

    companion object {
        operator fun <Output> invoke(
            block: suspend FlowCollector<Output>.() -> Unit,
        ): Effect<Output> = Effect(flow(block))

        fun <Output> none() = Effect<Output>(emptyFlow())

        fun <Action> of(vararg actions: Action): Effect<Action> =
            Effect { flowOf(*actions) }

        fun <Action> fromSuspend(func: suspend () -> Action): Effect<Action> =
            Effect { func.asFlow() }
    }

    fun <T> map(transform: (Output) -> T): Effect<T> = Effect(flow.map { transform(it) })

    @OptIn(FlowPreview::class)
    fun concatenate(vararg effects: Effect<Output>) {
        flow = flowOf(flow, *effects.map { it.flow }.toTypedArray()).flattenConcat()
    }

    @OptIn(FlowPreview::class)
    fun merge(vararg effects: Effect<Output>) {
        flow = flowOf(flow, *effects.map { it.flow }.toTypedArray()).flattenMerge()
    }

    suspend fun sink(): List<Output> {
        val outputs = mutableListOf<Output>()
        flow.toList(outputs)
        return outputs
    }
}

fun <State, Output> State.withNoEffect(): Result<State, Output> =
    Result(this, Effect.none())

fun <State, Output> State.withEffect(
    block: suspend FlowCollector<Output>.() -> Unit,
): Result<State, Output> =
    Result(this, Effect(flow(block)))

fun <State, Action : Any> State.withSuspendEffect(
    id: Any,
    cancelInFlight: Boolean = false,
    func: suspend () -> Action,
): ReduceResult<State, Action> =
    ReduceResult(this, Effect.fromSuspend(func).cancellable(id, cancelInFlight))

data class Result<out State, Action>(val state: State, val effect: Effect<Action>)
data class ReduceResult<out State, Action>(
    val state: State,
    val effect: Effect<Action>,
)