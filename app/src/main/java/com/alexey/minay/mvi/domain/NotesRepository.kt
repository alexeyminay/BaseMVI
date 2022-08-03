package com.alexey.minay.mvi.domain

import com.alexey.minay.mvi.domain.result.Result

interface NotesRepository {
    suspend fun getNotes(): Result<List<Note>>
}