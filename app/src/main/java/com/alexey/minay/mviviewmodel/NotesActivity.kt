package com.alexey.minay.mviviewmodel

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alexey.minay.mviviewmodel.presentation.NotesActor
import com.alexey.minay.mviviewmodel.presentation.NotesReducer
import com.alexey.minay.mviviewmodel.presentation.NotesStore
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NotesActivity : AppCompatActivity() {

    private val mStore by viewModels<NotesStore> {
        NotesStore.Factory(
            reducer = NotesReducer(),
            actor = NotesActor()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mStore.state.onEach { }
            .launchIn(lifecycleScope)
    }
}