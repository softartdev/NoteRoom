package com.softartdev.noteroom.ui.security.confirm

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.softartdev.noteroom.R
import com.softartdev.noteroom.ui.security.BaseDialogFragment

class ConfirmPasswordDialog : BaseDialogFragment(
        dialogLayoutRes = R.layout.dialog_set_password
), Observer<ConfirmResult> {

    private val confirmViewModel by viewModels<ConfirmViewModel> { viewModelFactory }

    private val passwordEditText: TextInputEditText
        get() = requireDialog().findViewById(R.id.set_password_edit_text)

    private val passwordTextInputLayout: TextInputLayout
        get() = requireDialog().findViewById(R.id.set_password_text_input_layout)

    private val repeatPasswordEditText: TextInputEditText
        get() = requireDialog().findViewById(R.id.repeat_set_password_edit_text)

    private val repeatPasswordTextInputLayout: TextInputLayout
        get() = requireDialog().findViewById(R.id.repeat_set_password_text_input_layout)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        confirmViewModel.confirmLiveData.observe(this, this)
    }

    override fun onOkClicked() = confirmViewModel.conformCheck(
            password = passwordEditText.editableText,
            repeatPassword = repeatPasswordEditText.editableText
    )

    override fun onChanged(confirmResult: ConfirmResult) {
        passwordTextInputLayout.error = null
        repeatPasswordTextInputLayout.error = null
        when (confirmResult) {
            ConfirmResult.Success -> dismiss()
            is ConfirmResult.EmptyPasswordError -> {
                passwordTextInputLayout.error = requireContext().getString(R.string.empty_password)
            }
            is ConfirmResult.PasswordsNoMatchError -> {
                repeatPasswordTextInputLayout.error = requireContext().getString(R.string.passwords_do_not_match)
            }
            is ConfirmResult.Error -> showError(confirmResult.message)
        }; Unit
    }

}