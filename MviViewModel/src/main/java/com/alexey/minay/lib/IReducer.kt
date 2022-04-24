package com.alexey.minay.lib

fun interface IReducer<Result : Any, State : Any> {
    fun State.reduce(result: Result): State
}