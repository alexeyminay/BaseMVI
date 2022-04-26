package com.alexey.minay.mviviewmodel.domain

import com.alexey.minay.mviviewmodel.domain.result.Result

interface NotesRepository {
    suspend fun getNotes(): Result<List<Note>>
}