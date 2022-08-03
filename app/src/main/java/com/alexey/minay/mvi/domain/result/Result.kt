package com.alexey.minay.mvi.domain.result

interface Result<out T> {
    class Success<T>(val data: T) : Result<T>
    class Error : Result<Nothing>
}