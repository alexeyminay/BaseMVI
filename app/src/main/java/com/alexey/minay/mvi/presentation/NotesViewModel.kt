package com.alexey.minay.mvi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NotesViewModel(
    val store: NotesStore
): ViewModel() {

    class Factory(
        private val reducer: NotesReducer,
        private val actor: NotesActor,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NotesViewModel(NotesStore(reducer, actor)) as T
        }

    }

}