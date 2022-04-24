package com.alexey.minay.mviviewmodel.presentation

import com.alexey.minay.lib.Executor

class NotesExecutor: Executor<NotesAction, NotesEvent, NotesState, NotesResult>() {

    override suspend fun execute(action: NotesAction, getState: () -> NotesState) {
        TODO("Not yet implemented")
    }
}