package com.alexey.minay.mviviewmodel.presentation

import com.alexey.minay.mviviewmodel.domain.Note

data class NotesState(
    val notes: List<Note>
) {
    companion object {
        fun default() = NotesState(notes = emptyList())
    }
}