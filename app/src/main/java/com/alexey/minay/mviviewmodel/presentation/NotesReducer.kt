package com.alexey.minay.mviviewmodel.presentation

import com.alexey.minay.lib.Reducer
import com.alexey.minay.mviviewmodel.presentation.state.NotesState

class NotesReducer : Reducer<NotesResult, NotesState> {

    override fun NotesState.reduce(result: NotesResult): NotesState {
        return when (result) {
            NotesResult.OpenFilter -> copy(isFilterOpened = true)
            NotesResult.SetErrorType -> copy(type = NotesState.Type.ERROR)
            is NotesResult.UpdateNotes -> copy(
                notes = result.notes,
                type = NotesState.Type.LIST
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
            filtersState = newFilterState
        )
    }

    private fun NotesState.changeWithDescriptionFilterState(): NotesState {
        val newFilterState = filtersState.copy(
            isWithDescriptionEnabled = !filtersState.isWithDescriptionEnabled
        )
        return copy(
            filtersState = newFilterState
        )
    }

}