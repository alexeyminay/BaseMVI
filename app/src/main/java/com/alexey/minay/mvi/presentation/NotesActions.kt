package com.alexey.minay.mvi.presentation

import com.alexey.minay.mvi.domain.Note

sealed interface NotesAction {
    object GetNotes : NotesAction
    object OpenFilter : NotesAction
    object ChangeWithDescriptionFilterState : NotesAction
    object ChangeShowPreviousFilterState : NotesAction
    object CloseFilters : NotesAction
}

sealed interface NotesResult {
    class UpdateNotes(val notes: List<Note>) : NotesResult
    object SetErrorType : NotesResult
    object OpenFilter : NotesResult
    object CloseFilters : NotesResult
    object ChangeWithDescriptionFilterState : NotesResult
    object ChangeShowPreviousFilterState : NotesResult
}

sealed interface NotesEffect {
    object ShowError : NotesEffect
    object HideRefreshing : NotesEffect
}