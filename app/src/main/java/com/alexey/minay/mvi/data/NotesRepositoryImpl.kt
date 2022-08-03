package com.alexey.minay.mvi.data

import com.alexey.minay.mvi.domain.Note
import com.alexey.minay.mvi.domain.NotesRepository
import com.alexey.minay.mvi.domain.result.Result

class NotesRepositoryImpl : NotesRepository {

    override suspend fun getNotes(): Result<List<Note>> {
        return listOf(Result.Success(FakeProvider.provide()), Result.Error()).random()
    }

}