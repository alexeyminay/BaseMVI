package com.alexey.minay.mvi.presentation

import com.alexey.minay.base_mvi.Store
import com.alexey.minay.mvi.presentation.state.NotesState

class NotesStore(
    reducer: NotesReducer,
    actor: NotesActor,
    initialState: NotesState = NotesState.default()
) : Store<NotesState, NotesAction, NotesEffect, NotesResult>(
    actor = actor,
    reducer = reducer,
    initialState = initialState
)