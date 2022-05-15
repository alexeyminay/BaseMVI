package com.alexey.minay.lib.stateManager

class StateStorage {

    private val mStates = mutableMapOf<Int, MutableList<StateStorageEntity>>()

    fun <State : Any, Result : Any> save(key: Int, state: State, result: Result) {
        mStates[key] = (mStates[key] ?: mutableListOf()).apply {
            add(
                StateStorageEntity.State(
                    state = state,
                    result = result
                )
            )
        }
    }

    fun create(key: Int) {
        if (mStates[key] == null) {
            mStates[key] = mutableListOf()
        }
    }

    fun getAll(): Map<Int, List<StateStorageEntity>> = mStates

    fun getKeysCount() = mStates.keys.count()

    fun remove(key: Int) = mStates.remove(key)

}