package com.alexey.minay.mviviewmodel.presentation.state

import com.alexey.minay.mviviewmodel.domain.Note
import java.util.*

data class NotesState(
    val notes: List<Note>,
    val type: Type,
    val filtersState: FiltersState,
    val isFilterOpened: Boolean
) {

    val filteredNotes: List<Note> = notes.filter(filtersState)

    private fun List<Note>.filter(filterState: FiltersState) =
        asSequence()
            .filter {
                when {
                    filterState.isWithDescriptionEnabled -> !it.description.isNullOrBlank()
                    else -> true
                }
            }
            .filter {
                when {
                    filterState.showPrevious -> true
                    else -> it.date.time >= Date().time
                }
            }.toList()

    enum class Type {
        INIT,
        LIST,
        ERROR
    }

    companion object {
        fun default() = NotesState(
            notes = emptyList(),
            type = Type.INIT,
            filtersState = FiltersState.default(),
            isFilterOpened = false
        )
    }
}