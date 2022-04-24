package com.alexey.minay.lib

fun interface Reducer<Result : Any, State : Any> {
    fun State.reduce(result: Result): State
}