package com.alexey.minay.lib

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

abstract class Executor<TAction : Any, TEvent : Any, TState : Any, TResult : Any> {

    protected val event get() = mEvent
    protected val reduce get() = mReduce
    protected val coroutineScope get() = mCoroutineScope

    private lateinit var mEvent: (TEvent) -> Unit
    private lateinit var mReduce: (TResult) -> Unit
    private var mCoroutineScope: CoroutineScope? = null

    fun init(event: (TEvent) -> Unit, reduce: (TResult) -> Unit, coroutineScope: CoroutineScope) {
        this.mEvent = event
        this.mReduce = reduce
        this.mCoroutineScope = coroutineScope
    }

    fun dispose() {
        mEvent = {}
        mReduce = {}
        mCoroutineScope?.cancel()
        mCoroutineScope = null
    }

    abstract suspend fun execute(action: TAction, getState: () -> TState)
}