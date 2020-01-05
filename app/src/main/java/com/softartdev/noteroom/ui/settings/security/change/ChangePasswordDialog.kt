package com.softartdev.noteroom.ui.settings.security.change

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

class ChangePasswordDialog : BaseDialogFragment(
        dialogLayoutRes = R.layout.dialog_change_password
), Observer<ChangeResult> {

    private val changeViewModel by viewModels<ChangeViewModel> { viewModelFactory }

    private val progressBar: ProgressBar
        get() = requireDialog().findViewById(R.id.dialog_change_progress_bar)

    private val oldPasswordEditText: TextInputEditText
        get() = requireDialog().findViewById(R.id.old_password_edit_text)

    private val oldPasswordTextInputLayout: TextInputLayout
        get() = requireDialog().findViewById(R.id.old_password_text_input_layout)

    private val newPasswordEditText: TextInputEditText
        get() = requireDialog().findViewById(R.id.new_password_edit_text)

    private val newPasswordTextInputLayout: TextInputLayout
        get() = requireDialog().findViewById(R.id.new_password_text_input_layout)

    private val repeatPasswordEditText: TextInputEditText
        get() = requireDialog().findViewById(R.id.repeat_new_password_edit_text)

    private val repeatPasswordTextInputLayout: TextInputLayout
        get() = requireDialog().findViewById(R.id.repeat_new_password_text_input_layout)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        changeViewModel.changeLiveData.observe(this, this)
    }

    override fun onOkClicked() = changeViewModel.checkChange(
            oldPassword = oldPasswordEditText.editableText,
            newPassword = newPasswordEditText.editableText,
            repeatNewPassword = repeatPasswordEditText.editableText
    )

    override fun onChanged(changeResult: ChangeResult) {
        progressBar.invisible()
        oldPasswordTextInputLayout.error = null
        newPasswordTextInputLayout.error = null
        repeatPasswordTextInputLayout.error = null
        when (changeResult) {
            ChangeResult.Loading -> progressBar.visible()
            ChangeResult.Success -> dismiss()
            ChangeResult.OldEmptyPasswordError -> {
                oldPasswordTextInputLayout.error = requireContext().getString(R.string.empty_password)
            }
            ChangeResult.NewEmptyPasswordError -> {
                newPasswordTextInputLayout.error = requireContext().getString(R.string.empty_password)
            }
            ChangeResult.PasswordsNoMatchError -> {
                repeatPasswordTextInputLayout.error = requireContext().getString(R.string.passwords_do_not_match)
            }
            ChangeResult.IncorrectPasswordError -> {
                oldPasswordTextInputLayout.error = requireContext().getString(R.string.incorrect_password)
            }
            is ChangeResult.Error -> showError(changeResult.message)
        }
    }

}