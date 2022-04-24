package com.alexey.minay.mviviewmodel.presentation

import com.alexey.minay.lib.ViewModelStore

class NotesStore(
    reducer: NotesReducer,
    executor: NotesActor,
    initialState: NotesState = NotesState.default()
) : ViewModelStore<NotesState, NotesAction, NotesEffect, NotesResult>(executor, reducer, initialState)