package com.alexey.minay.mviviewmodel.presentation

sealed interface NotesAction
sealed interface NotesResult {
    object SampleResult : NotesResult
}

sealed interface NotesEffect {
    object Sample : NotesEffect
}