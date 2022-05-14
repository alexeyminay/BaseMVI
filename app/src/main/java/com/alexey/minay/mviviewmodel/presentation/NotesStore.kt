package com.alexey.minay.mviviewmodel.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexey.minay.lib.Store
import com.alexey.minay.lib.stateManager.StateManager
import com.alexey.minay.mviviewmodel.presentation.state.NotesState

class NotesStore(
    reducer: NotesReducer,
    actor: NotesActor,
    initialState: NotesState = NotesState.default(),
    stateManager: StateManager
) : Store<NotesState, NotesAction, NotesEffect, NotesResult>(
    actor = actor,
    reducer = reducer,
    initialState = initialState,
    stateManager = stateManager
) {

    class Factory(
        private val reducer: NotesReducer,
        private val actor: NotesActor,
        private val stateManager: StateManager
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NotesStore(reducer, actor, stateManager = stateManager) as T
        }

    }

}