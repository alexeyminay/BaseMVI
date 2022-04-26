package com.alexey.minay.mviviewmodel.domain

import java.util.*

data class Note(
    val id: Long,
    val title: String,
    val date: Date,
    val description: String?
)