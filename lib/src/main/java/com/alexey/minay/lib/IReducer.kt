package com.alexey.minay.lib

fun interface IReducer<TResult : Any, TState : Any> {
    fun TState.reduce(result: TResult): TState
}