package com.alexey.minay.mvi.data

import com.alexey.minay.mvi.domain.Note
import java.util.*

object FakeProvider {

    private const val DAY_IN_MS = 3600 * 24 * 1000

    fun provide() = mutableListOf(
        Note(
            id = 0,
            title = "Sample note 1",
            date = Date(Date().time - DAY_IN_MS),
            description = "Description 1"
        ),
        Note(
            id = 1,
            title = "Sample note 2",
            date = Date(Date().time + DAY_IN_MS),
            description = "Description 2"
        ),
        Note(
            id = 2,
            title = "Sample note 3",
            date = Date(Date().time + 2 * DAY_IN_MS),
            description = null
        )
    )

}