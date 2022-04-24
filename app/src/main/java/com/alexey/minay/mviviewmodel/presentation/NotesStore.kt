package com.alexey.minay.mviviewmodel.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexey.minay.lib.ViewModelStore

class NotesStore(
    reducer: NotesReducer,
    actor: NotesActor,
    initialState: NotesState = NotesState.default()
) : ViewModelStore<NotesState, NotesAction, NotesEffect, NotesResult>(
    actor,
    reducer,
    initialState
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