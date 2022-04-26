package com.alexey.minay.mviviewmodel.data

import com.alexey.minay.mviviewmodel.domain.Note
import com.alexey.minay.mviviewmodel.domain.NotesRepository
import com.alexey.minay.mviviewmodel.domain.result.Result

class NotesRepositoryImpl : NotesRepository {

    override suspend fun getNotes(): Result<List<Note>> {
        return listOf(Result.Success(FakeProvider.provide()), Result.Error()).random()
    }

}