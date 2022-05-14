package com.alexey.minay.lib.stateManager

class StateStorage {

    private val mStates = mutableMapOf<Int, MutableList<Any>>()

    fun <State : Any> save(key: Int, state: State) {
        mStates[key] = (mStates[key] ?: mutableListOf()).apply {
            add(state)
        }
    }

    fun create(key: Int) {
        if (mStates[key] == null) {
            mStates[key] = mutableListOf()
        }
    }

    fun getAll(): Map<Int, List<Any>> = mStates

    fun getKeysCount() = mStates.keys.count()

    fun remove(key: Int) = mStates.remove(key)

}