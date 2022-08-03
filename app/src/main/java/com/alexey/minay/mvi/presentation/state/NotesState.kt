package com.alexey.minay.mvi.presentation.state

import com.alexey.minay.mvi.domain.Note

data class NotesState(
    val notes: List<Note>,
    val filteredNotes: List<Note>,
    val type: Type,
    val filtersState: FiltersState,
    val isFilterOpened: Boolean
) {

    enum class Type {
        INIT,
        LIST,
        ERROR
    }

    companion object {
        fun default() = NotesState(
            notes = emptyList(),
            filteredNotes = emptyList(),
            type = Type.INIT,
            filtersState = FiltersState.default(),
            isFilterOpened = false
        )
    }
}