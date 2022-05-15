package com.alexey.minay.lib.stateManager

sealed class StateStorageEntity {
    data class State(
        val state: Any,
        val result: Any
    ) : StateStorageEntity()

    val stateOrNull = this as? State
}