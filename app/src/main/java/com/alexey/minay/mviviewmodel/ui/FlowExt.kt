package com.alexey.minay.mviviewmodel.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.*

context(LifecycleOwner)
fun <ParentState, ChildState> StateFlow<ParentState>.render(
    mapper: (ParentState) -> ChildState,
    action: suspend (ChildState) -> Unit
) = map { mapper(it) }
    .distinctUntilChanged()
    .onEach { action(it) }
    .flowWithLifecycle(this@LifecycleOwner.lifecycle)
    .launchIn(this@LifecycleOwner.lifecycleScope)
