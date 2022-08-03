package com.alexey.minay.base_mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class Store<State : Any, Action : Any, Effect : Any, Result : Any>(
    private val actor: Actor<Action, Effect, State, Result>,
    private val reducer: Reducer<Result, State>,
    initialState: State
) {

    val state: StateFlow<State> by lazy(LazyThreadSafetyMode.NONE) { mState.asStateFlow() }
    val effects: SharedFlow<Effect> by lazy(LazyThreadSafetyMode.NONE) { mEvent.asSharedFlow() }

    private val mState = MutableStateFlow(initialState)
    private val mEvent = MutableSharedFlow<Effect>()
    private val mScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        actor.init(
            event = ::event,
            reduce = ::reduce,
            reduceBlocking = ::reduceBlocking,
            coroutineScope = mScope
        )
    }

    fun accept(action: Action) {
        mScope.launch {
            actor.execute(action, ::getState)
        }
    }

    fun onCleared() {
        actor.dispose()
    }

    private fun event(event: Effect) {
        mScope.launch {
            mEvent.emit(event)
        }
    }

    private fun getState(): State = state.value

    private fun reduce(result: Result) {
        mScope.launch {
            reduceBlocking(result)
        }
    }

    private fun reduceBlocking(result: Result) {
        with(reducer) { mState.value = getState().reduce(result) }
    }

}
