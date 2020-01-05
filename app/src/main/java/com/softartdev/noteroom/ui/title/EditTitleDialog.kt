package com.softartdev.noteroom.ui.title

import android.os.Bundle
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.softartdev.noteroom.R
import com.softartdev.noteroom.ui.base.BaseDialogFragment
import com.softartdev.noteroom.util.invisible
import com.softartdev.noteroom.util.visible

class EditTitleDialog : BaseDialogFragment(
        dialogLayoutRes = R.layout.dialog_edit_title
), Observer<EditTitleResult> {

    private val editTitleViewModel by viewModels<EditTitleViewModel> { viewModelFactory }

    private val noteId: Long
        get() = requireNotNull(arguments?.getLong(ARG_NOTE_ID))

    private val progressBar: ProgressBar
        get() = requireDialog().findViewById(R.id.edit_title_progress_bar)

    private val textInputLayout: TextInputLayout
        get() = requireDialog().findViewById(R.id.edit_title_text_input_layout)

    private val editText: TextInputEditText
        get() = requireDialog().findViewById(R.id.edit_title_text_input)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        editTitleViewModel.editTitleLiveData.observe(this, this)
        editTitleViewModel.loadTitle(noteId)
    }

    override fun onOkClicked() = editTitleViewModel.editTitle(
            newTitle = editText.text?.toString().orEmpty()
    )

    override fun onChanged(editTitleResult: EditTitleResult) {
        progressBar.invisible()
        textInputLayout.error = null
        when (editTitleResult) {
            EditTitleResult.Loading -> progressBar.visible()
            EditTitleResult.Success -> dismiss()
            EditTitleResult.EmptyTitleError -> {
                textInputLayout.error = getString(R.string.empty_title)
            }
            is EditTitleResult.Error -> showError(editTitleResult.message)
        }
    }

    companion object {
        private const val ARG_NOTE_ID = "arg_note_id"

        fun create(noteId: Long): EditTitleDialog = EditTitleDialog().apply {
            arguments = Bundle().apply {
                putLong(ARG_NOTE_ID, noteId)
            }
        }
    }
}