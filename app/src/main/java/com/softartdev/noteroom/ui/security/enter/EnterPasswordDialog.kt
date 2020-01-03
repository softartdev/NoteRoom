package com.softartdev.noteroom.ui.security.enter

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.softartdev.noteroom.R
import com.softartdev.noteroom.ui.security.BaseDialogFragment

class EnterPasswordDialog : BaseDialogFragment(
        dialogLayoutRes = R.layout.dialog_password
), Observer<EnterResult> {

    private val enterViewModel by viewModels<EnterViewModel> { viewModelFactory }

    private val passwordEditText: TextInputEditText
        get() = requireDialog().findViewById(R.id.enter_password_edit_text)

    private val passwordTextInputLayout: TextInputLayout
        get() = requireDialog().findViewById(R.id.enter_password_text_input_layout)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        enterViewModel.enterLiveData.observe(this, this)
    }

    override fun onOkClicked() = enterViewModel.enterCheck(
            password = passwordEditText.editableText
    )

    override fun onChanged(enterResult: EnterResult) {
        passwordTextInputLayout.error = null
        when (enterResult) {
            EnterResult.Success -> dismiss()
            is EnterResult.EmptyPasswordError -> {
                passwordTextInputLayout.error = requireContext().getString(R.string.empty_password)
            }
            is EnterResult.IncorrectPasswordError -> {
                passwordTextInputLayout.error = requireContext().getString(R.string.incorrect_password)
            }
            is EnterResult.Error -> showError(enterResult.message)
        }
    }

}