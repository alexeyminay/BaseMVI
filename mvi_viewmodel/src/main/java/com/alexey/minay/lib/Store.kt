package com.alexey.minay.lib

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexey.minay.lib.stateManager.StateManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class Store<State : Any, Action : Any, Effect : Any, Result : Any>(
    private val actor: Actor<Action, Effect, State, Result>,
    private val reducer: Reducer<Result, State>,
    private val stateManager: StateManager,
    initialState: State
) : ViewModel() {

    val state: StateFlow<State> by lazy(LazyThreadSafetyMode.NONE) { mState.asStateFlow() }
    val effects: SharedFlow<Effect> by lazy(LazyThreadSafetyMode.NONE) { mEvent.asSharedFlow() }

    private val mState = MutableStateFlow(initialState)
    private val mEvent = MutableSharedFlow<Effect>()

    init {
        actor.init(
            event = ::event,
            reduce = ::reduce,
            reduceBlocking = ::reduceBlocking,
            coroutineScope = viewModelScope
        )
        viewModelScope.launch {
            stateManager.register(this@Store)
        }
    }

    fun accept(action: Action) {
        viewModelScope.launch {
            actor.execute(action, ::getState)
        }
    }

    override fun onCleared() {
        super.onCleared()
        actor.dispose()
        viewModelScope.launch {
            stateManager.release(this@Store)
        }
    }

    private fun event(event: Effect) {
        viewModelScope.launch {
            mEvent.emit(event)
        }
    }

    private fun getState(): State = state.value

    private fun reduce(result: Result) {
        viewModelScope.launch {
            reduceBlocking(result)
        }
    }

    private fun reduceBlocking(result: Result) {
        with(reducer) { mState.value = getState().reduce(result) }
        viewModelScope.launch {
            stateManager.onStateChanged(this@Store, getState(), result)
        }
    }

}
