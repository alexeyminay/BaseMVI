package com.alexey.minay.lib.stateManager

import com.alexey.minay.lib.Store

class StateManager private constructor(
    private val publisher: StatePublisher,
    private val converter: StateConverter,
    private val storage: StateStorage
) : IStateManager {

    init {
        publisher.setGetMessageCallback(getMessageCallback = ::getMessage)
    }

    override suspend fun <State : Any, Action : Any, Effect : Any, Result : Any> register(
        store: Store<State, Action, Effect, Result>
    ) {
        storage.create(store.hashCode())
        checkKeys()
    }

    override suspend fun <State : Any, Action : Any, Effect : Any, Result : Any> release(
        store: Store<State, Action, Effect, Result>
    ) {
        storage.remove(store.hashCode())
        checkKeys()
    }

    override suspend fun <State : Any, Action : Any, Effect : Any, Result : Any> onStateChanged(
        store: Store<State, Action, Effect, Result>,
        state: State,
        result: Result
    ) {
        storage.save(store.hashCode(), state)
        checkKeys()
    }

    private suspend fun checkKeys() {
        if (storage.getKeysCount() > 0) {
            if (!publisher.isContinuePublish) {
                publisher.start()
            }
        } else {
            publisher.stop()
        }
    }

    private fun getMessage(): String {
        val state = storage.getAll()
        return converter.convert(state)
    }

    companion object {
        private val mStateManager by lazy {
            val storage = StateStorage()
            val converter = StateConverter()
            val publisher = StatePublisher()

            StateManager(
                publisher = publisher,
                converter = converter,
                storage = storage
            )
        }

        fun getInstance() = mStateManager
    }

}