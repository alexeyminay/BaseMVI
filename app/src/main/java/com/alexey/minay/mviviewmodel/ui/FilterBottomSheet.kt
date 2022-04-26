package com.alexey.minay.mviviewmodel.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.alexey.minay.mviviewmodel.databinding.FragmentFilterBinding
import com.alexey.minay.mviviewmodel.presentation.NotesAction
import com.alexey.minay.mviviewmodel.presentation.NotesStore
import com.alexey.minay.mviviewmodel.presentation.state.NotesState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FilterBottomSheet : BottomSheetDialogFragment() {

    private val mStore by activityViewModels<NotesStore>()
    private var mBinding: FragmentFilterBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentFilterBinding.inflate(inflater)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFilters()
        subscribeToStore()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mStore.accept(NotesAction.CloseFilters)
    }

    private fun initFilters() = with(mBinding!!) {
        withDescription.setOnClickListener { mStore.accept(NotesAction.ChangeWithDescriptionFilterState) }
        showPrevious.setOnClickListener { mStore.accept(NotesAction.ChangeShowPreviousFilterState) }
    }

    private fun subscribeToStore() = with(viewLifecycleOwner) {
        mStore.state
            .flowWithLifecycle(lifecycle)
            .onEach(::render)
            .launchIn(lifecycleScope)
    }

    private fun render(state: NotesState) = with(mBinding!!) {
        withDescription.isChecked = state.filtersState.isWithDescriptionEnabled
        showPrevious.isChecked = state.filtersState.showPrevious
    }

    companion object {
        fun newInstance() = FilterBottomSheet()
    }

}