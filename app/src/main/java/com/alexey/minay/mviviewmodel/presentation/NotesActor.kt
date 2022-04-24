package com.alexey.minay.mviviewmodel.presentation

import com.alexey.minay.lib.Actor

class NotesActor: Actor<NotesAction, NotesEffect, NotesState, NotesResult>() {

    override suspend fun execute(action: NotesAction, getState: () -> NotesState) {
        emit { NotesEffect.Sample }
    }
}