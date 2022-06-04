package com.alexey.minay.mviviewmodel.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.alexey.minay.lib.stateManager.StateManager
import com.alexey.minay.mviviewmodel.R
import com.alexey.minay.mviviewmodel.data.NotesRepositoryImpl
import com.alexey.minay.mviviewmodel.databinding.ActivityMainBinding
import com.alexey.minay.mviviewmodel.domain.Note
import com.alexey.minay.mviviewmodel.presentation.*
import com.alexey.minay.mviviewmodel.presentation.state.NotesState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NotesActivity : AppCompatActivity() {

    private val mAdapter = NotesAdapter()
    private val mBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(
            layoutInflater
        )
    }

    private val mStore by viewModels<NotesStore> {
        NotesStore.Factory(
            reducer = NotesReducer(),
            actor = NotesActor(NotesRepositoryImpl()),
            stateManager = StateManager.getInstance()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initList()
        initMenu()
        initRefreshLayout()
        subscribeToStore()

        mStore.accept(NotesAction.GetNotes)
    }

    private fun initList() {
        mBinding.notes.adapter = mAdapter
    }

    private fun initMenu() {
        mBinding.toolbar.inflateMenu(R.menu.menu_filter)
        mBinding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.filter -> mStore.accept(NotesAction.OpenFilter)
                else -> Unit
            }
            return@setOnMenuItemClickListener false
        }
    }

    private fun initRefreshLayout() {
        mBinding.refresher.setOnRefreshListener { mStore.accept(NotesAction.GetNotes) }
    }

    private fun subscribeToStore() {
        mStore.effects
            .flowWithLifecycle(lifecycle)
            .onEach(::handleEffects)
            .launchIn(lifecycleScope)

        with(mStore.state) {
            render(NotesState::type, ::renderType)
            render(NotesState::filteredNotes, ::renderList)
            render(NotesState::isFilterOpened, ::renderFilter)
        }
    }

    private fun renderType(state: NotesState.Type) = with(mBinding) {
        when (state) {
            NotesState.Type.INIT -> {
                progress.isVisible = true
                info.isVisible = false
                notes.isVisible = false
            }
            NotesState.Type.LIST -> {
                progress.isVisible = false
                info.isVisible = false
                notes.isVisible = true
            }
            NotesState.Type.ERROR -> {
                progress.isVisible = false
                info.isVisible = true
                notes.isVisible = false
                info.text = "Error try refreshing..."
            }
        }
    }

    private fun renderList(state: List<Note>) {
        mAdapter.submitList(state)
    }

    private fun renderFilter(state: Boolean) {
        if (state) {
            FilterBottomSheet.newInstance().show(supportFragmentManager, FILTER_TAG)
        }
    }

    private fun handleEffects(effect: NotesEffect) {
        when (effect) {
            NotesEffect.ShowError ->
                Toast.makeText(this, "Error..", Toast.LENGTH_SHORT).show()
            NotesEffect.HideRefreshing ->
                mBinding.refresher.isRefreshing = false
        }
    }

    companion object {
        private const val FILTER_TAG = "filter_tag"
    }

}