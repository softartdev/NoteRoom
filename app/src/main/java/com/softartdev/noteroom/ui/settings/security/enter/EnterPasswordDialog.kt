package com.softartdev.noteroom.ui.settings.security.enter

import android.os.Bundle
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.softartdev.noteroom.R
import com.softartdev.noteroom.ui.base.BaseDialogFragment
import com.softartdev.noteroom.util.invisible
import com.softartdev.noteroom.util.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class EnterPasswordDialog : BaseDialogFragment(
        dialogLayoutRes = R.layout.dialog_password
), Observer<EnterResult> {

    private val enterViewModel by viewModel<EnterViewModel>()

    private val progressBar: ProgressBar
        get() = requireDialog().findViewById(R.id.enter_progress_bar)

    private val passwordEditText: TextInputEditText
        get() = requireDialog().findViewById(R.id.enter_password_edit_text)

    private val passwordTextInputLayout: TextInputLayout
        get() = requireDialog().findViewById(R.id.enter_password_text_input_layout)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        enterViewModel.resultLiveData.observe(this as LifecycleOwner, this)
    }

    override fun onOkClicked() = enterViewModel.enterCheck(
            password = passwordEditText.editableText
    )

    override fun onChanged(enterResult: EnterResult) {
        progressBar.invisible()
        passwordTextInputLayout.error = null
        when (enterResult) {
            EnterResult.Loading -> progressBar.visible()
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