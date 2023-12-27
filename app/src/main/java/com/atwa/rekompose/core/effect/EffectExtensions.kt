package com.atwa.rekompose.core.effect

data class AffectedState<out State, Action>(val state: State, val effect: Effect<Action>)

fun <State, Action> State.withNoEffect(): AffectedState<State, Action> =
    AffectedState(this, Effect.none())

fun <State, Action> State.withEffect(effect: Effect<Action>): AffectedState<State, Action> =
    AffectedState(this, Effect.fromSuspend { effect.run() })

fun <State, Action : Any> State.withSuspendEffect(func: suspend () -> Action): AffectedState<State, Action> =
    AffectedState(this, Effect.fromSuspend(func))


