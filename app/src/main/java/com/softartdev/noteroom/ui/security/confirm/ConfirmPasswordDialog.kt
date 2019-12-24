package com.softartdev.noteroom.ui.security.confirm

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.softartdev.noteroom.R
import dagger.android.support.DaggerAppCompatDialogFragment
import kotlinx.android.synthetic.main.dialog_set_password.*
import javax.inject.Inject

class ConfirmPasswordDialog : DaggerAppCompatDialogFragment(), Observer<ConfirmResult> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val confirmViewModel by viewModels<ConfirmViewModel> { viewModelFactory }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        confirmViewModel.confirmLiveData.observe(this, this)
        return AlertDialog.Builder(requireActivity())
                .setView(layoutInflater.inflate(R.layout.dialog_set_password, null))
                .setPositiveButton(android.R.string.ok) { _, _ -> conformPassword() }
                .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
                .create()
    }

    private fun conformPassword() = confirmViewModel.conformCheck(
            password = set_password_edit_text.text,
            repeatPassword = repeat_set_password_edit_text.text
    )

    override fun onChanged(confirmResult: ConfirmResult) {
        set_password_text_input_layout.error = null
        repeat_set_password_text_input_layout.error = null
        when (confirmResult) {
            ConfirmResult.Success -> dismiss()
            is ConfirmResult.EmptyPasswordError -> {
                set_password_text_input_layout.error = requireContext().getString(R.string.empty_password)
            }
            is ConfirmResult.PasswordsNoMatchError -> {
                repeat_set_password_text_input_layout.error = requireContext().getString(R.string.passwords_do_not_match)
            }
            is ConfirmResult.Error -> showError(confirmResult.message)
        }
    }

    private fun showError(message: String?) = with(AlertDialog.Builder(requireContext())) {
        setTitle(android.R.string.dialog_alert_title)
        setMessage(message)
        setNeutralButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
    }.show()
}