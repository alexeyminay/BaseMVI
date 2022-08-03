package com.alexey.minay.mvi.presentation

import com.alexey.minay.base_mvi.Reducer
import com.alexey.minay.mvi.domain.Note
import com.alexey.minay.mvi.presentation.state.FiltersState
import com.alexey.minay.mvi.presentation.state.NotesState
import java.util.*

class NotesReducer : Reducer<NotesResult, NotesState> {

    override fun NotesState.reduce(result: NotesResult): NotesState {
        return when (result) {
            NotesResult.OpenFilter -> copy(isFilterOpened = true)
            NotesResult.SetErrorType -> copy(type = NotesState.Type.ERROR)
            is NotesResult.UpdateNotes -> copy(
                notes = result.notes,
                type = NotesState.Type.LIST,
                filteredNotes = result.notes.filter(filtersState)
            )
            NotesResult.ChangeShowPreviousFilterState ->
                changeShowPreviousFilterState()
            NotesResult.ChangeWithDescriptionFilterState ->
                changeWithDescriptionFilterState()
            NotesResult.CloseFilters ->
                copy(isFilterOpened = false)
        }
    }

    private fun NotesState.changeShowPreviousFilterState(): NotesState {
        val newFilterState = filtersState.copy(
            showPrevious = !filtersState.showPrevious
        )
        return copy(
            filtersState = newFilterState,
            filteredNotes = notes.filter(newFilterState)
        )
    }

    private fun NotesState.changeWithDescriptionFilterState(): NotesState {
        val newFilterState = filtersState.copy(
            isWithDescriptionEnabled = !filtersState.isWithDescriptionEnabled
        )
        return copy(
            filtersState = newFilterState,
            filteredNotes = notes.filter(newFilterState)
        )
    }

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

}