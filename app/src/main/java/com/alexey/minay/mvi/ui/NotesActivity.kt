package com.alexey.minay.mvi.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.alexey.minay.mvi.R
import com.alexey.minay.mvi.data.NotesRepositoryImpl
import com.alexey.minay.mvi.databinding.ActivityMainBinding
import com.alexey.minay.mvi.presentation.*
import com.alexey.minay.mvi.presentation.state.NotesState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NotesActivity : AppCompatActivity() {

    private val mAdapter = NotesAdapter()
    private val mBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(
            layoutInflater
        )
    }

    private val mViewModel by viewModels<NotesViewModel> {
        NotesViewModel.Factory(
            reducer = NotesReducer(),
            actor = NotesActor(NotesRepositoryImpl())
        )
    }
    private val mStore get() = mViewModel.store

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
        mStore.state
            .flowWithLifecycle(lifecycle)
            .onEach(::render)
            .launchIn(lifecycleScope)

        mStore.effects
            .flowWithLifecycle(lifecycle)
            .onEach(::handleEffects)
            .launchIn(lifecycleScope)
    }

    private fun render(state: NotesState) {
        renderType(state)
        renderList(state)
        renderFilter(state)
    }

    private fun renderType(state: NotesState) = with(mBinding) {
        when (state.type) {
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
                info.text = "Error..."
            }
        }
    }

    private fun renderList(state: NotesState) {
        mAdapter.submitList(state.filteredNotes)
    }

    private fun renderFilter(state: NotesState) {
        val isAlreadyOpened = supportFragmentManager.findFragmentByTag(FILTER_TAG) != null

        if (state.isFilterOpened && !isAlreadyOpened) {
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