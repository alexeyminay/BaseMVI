package com.alexey.minay.lib

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class ViewModelStore<State : Any, Action : Any, Effect : Any, Result : Any>(
    private val actor: Actor<Action, Effect, State, Result>,
    private val reducer: Reducer<Result, State>,
    initialState: State
) : ViewModel() {

    val state: StateFlow<State> by lazy(LazyThreadSafetyMode.NONE) { mState.asStateFlow() }
    val event: SharedFlow<Effect> by lazy(LazyThreadSafetyMode.NONE) { mEvent.asSharedFlow() }

    private val mState = MutableStateFlow(initialState)
    private val mEvent = MutableSharedFlow<Effect>(extraBufferCapacity = 1)

    init {
        actor.init(
            event = ::event,
            reduce = ::reduce,
            coroutineScope = viewModelScope
        )
    }

    fun accept(action: Action) {
        viewModelScope.launch {
            actor.execute(action, ::getState)
        }
    }

    override fun onCleared() {
        super.onCleared()
        actor.dispose()
    }

    private fun event(event: Effect) {
        mEvent.tryEmit(event)
    }

    private fun getState(): State = state.value

    private fun reduce(result: Result) {
        viewModelScope.launch {
            with(reducer) { mState.value = getState().reduce(result) }
        }
    }

}
