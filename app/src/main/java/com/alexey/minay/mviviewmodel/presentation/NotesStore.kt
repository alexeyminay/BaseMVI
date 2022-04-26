package com.alexey.minay.mviviewmodel.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexey.minay.lib.Store
import com.alexey.minay.mviviewmodel.presentation.state.NotesState

class NotesStore(
    reducer: NotesReducer,
    actor: NotesActor,
    initialState: NotesState = NotesState.default()
) : Store<NotesState, NotesAction, NotesEffect, NotesResult>(
    actor = actor,
    reducer = reducer,
    initialState = initialState
) {

    class Factory(
        private val reducer: NotesReducer,
        private val actor: NotesActor,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NotesStore(reducer, actor) as T
        }

    }

}