package com.alexey.minay.base_mvi

fun interface Reducer<Result : Any, State : Any> {
    fun State.reduce(result: Result): State
}