package com.alexey.minay.mviviewmodel.presentation

import com.alexey.minay.lib.Store

class NotesStore(
    reducer: NotesReducer,
    executor: NotesExecutor,
    initialState: NotesState = NotesState.default()
) : Store<NotesState, NotesAction, NotesEvent, NotesResult>(executor, reducer, initialState)