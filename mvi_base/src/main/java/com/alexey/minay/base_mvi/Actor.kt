package com.alexey.minay.base_mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

abstract class Actor<Action : Any, Effect : Any, State : Any, Result : Any> {

    protected val coroutineScope get() = mCoroutineScope

    private lateinit var mEffect: (Effect) -> Unit
    private lateinit var mReduce: (Result) -> Unit
    private lateinit var mReduceBlocking: (Result) -> Unit
    private var mCoroutineScope: CoroutineScope? = null

    fun init(
        event: (Effect) -> Unit,
        reduce: (Result) -> Unit,
        reduceBlocking: (Result) -> Unit,
        coroutineScope: CoroutineScope
    ) {
        this.mEffect = event
        this.mReduce = reduce
        this.mCoroutineScope = coroutineScope
        this.mReduceBlocking = reduceBlocking
    }

    fun dispose() {
        mEffect = {}
        mReduce = {}
        mReduceBlocking = {}
        mCoroutineScope?.cancel()
        mCoroutineScope = null
    }

    protected fun emit(effectProvider: () -> Effect) {
        mEffect(effectProvider())
    }

    protected fun reduce(resultProvider: () -> Result) {
        mReduce(resultProvider())
    }

    protected suspend fun reduceSuspend(resultProvider: () -> Result) {
        mReduceBlocking(resultProvider())
    }

    abstract suspend fun execute(action: Action, getState: () -> State)

}