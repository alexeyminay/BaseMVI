package com.alexey.minay.mviviewmodel.presentation

import com.alexey.minay.lib.Actor
import com.alexey.minay.mviviewmodel.domain.NotesRepository
import com.alexey.minay.mviviewmodel.domain.result.Result
import com.alexey.minay.mviviewmodel.presentation.state.NotesState

class NotesActor(
    private val repository: NotesRepository
) : Actor<NotesAction, NotesEffect, NotesState, NotesResult>() {

    override suspend fun execute(action: NotesAction, getState: () -> NotesState) {
        when (action) {
            NotesAction.GetNotes ->
                getNotes(getState)
            NotesAction.OpenFilter ->
                reduce { NotesResult.OpenFilter }
            NotesAction.ChangeShowPreviousFilterState ->
                reduce { NotesResult.ChangeShowPreviousFilterState }
            NotesAction.ChangeWithDescriptionFilterState ->
                reduce { NotesResult.ChangeWithDescriptionFilterState }
            NotesAction.CloseFilters ->
                reduce { NotesResult.CloseFilters }
        }
    }

    private suspend fun getNotes(getState: () -> NotesState) {
        when (val result = repository.getNotes()) {
            is Result.Success -> reduce { NotesResult.UpdateNotes(result.data) }
            is Result.Error -> when (getState().notes.isEmpty()) {
                true -> reduce { NotesResult.SetErrorType }
                else -> emit { NotesEffect.ShowError }
            }
        }

        emit { NotesEffect.HideRefreshing }
    }

}