package com.alexey.minay.mvi.presentation.state

data class FiltersState(
    val isWithDescriptionEnabled: Boolean,
    val showPrevious: Boolean
) {
    companion object {
        fun default() = FiltersState(
            isWithDescriptionEnabled = false,
            showPrevious = true
        )
    }
}