package com.alexey.minay.lib

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class Store<TState : Any, TAction : Any, TEffect : Any, TResult : Any>(
    private val actor: Actor<TAction, TEffect, TState, TResult>,
    private val reducer: IReducer<TResult, TState>,
    initialState: TState
) : ViewModel() {

    val state: StateFlow<TState> by lazy(LazyThreadSafetyMode.NONE) { mState.asStateFlow() }
    val event: SharedFlow<TEffect> by lazy(LazyThreadSafetyMode.NONE) { mEvent.asSharedFlow() }

    private val mState = MutableStateFlow(initialState)
    private val mEvent = MutableSharedFlow<TEffect>(extraBufferCapacity = 1)

    init {
        actor.init(
            event = ::event,
            reduce = {
                viewModelScope.launch {
                    reducer.run { mState.value = getState().reduce(it) }
                }
            },
            coroutineScope = viewModelScope
        )
    }

    fun accept(action: TAction) {
        viewModelScope.launch {
            actor.execute(action, ::getState)
        }
    }

    override fun onCleared() {
        super.onCleared()
        actor.dispose()
    }

    private fun event(event: TEffect) {
        mEvent.tryEmit(event)
    }

    private fun getState(): TState = state.value

}
