package com.alexey.minay.lib.stateManager

import com.alexey.minay.lib.Store

interface IStateManager {
    suspend fun <State : Any, Action : Any, Effect : Any, Result : Any> register(
        store: Store<State, Action, Effect, Result>
    )

    suspend fun <State : Any, Action : Any, Effect : Any, Result : Any> release(
        store: Store<State, Action, Effect, Result>
    )

    suspend fun <State : Any, Action : Any, Effect : Any, Result : Any> onStateChanged(
        store: Store<State, Action, Effect, Result>, state: State, result: Result
    )
}