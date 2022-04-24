package com.alexey.minay.mviviewmodel.presentation

import com.alexey.minay.lib.Store

class NotesStore(
    reducer: NotesReducer,
    executor: NotesActor,
    initialState: NotesState = NotesState.default()
) : Store<NotesState, NotesAction, NotesEffect, NotesResult>(executor, reducer, initialState)