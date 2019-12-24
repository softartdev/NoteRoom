package com.softartdev.noteroom.ui.security.change

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.softartdev.noteroom.R
import dagger.android.support.DaggerAppCompatDialogFragment
import kotlinx.android.synthetic.main.dialog_change_password.*
import javax.inject.Inject

class ChangePasswordDialog : DaggerAppCompatDialogFragment(), Observer<ChangeResult> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val changeViewModel by viewModels<ChangeViewModel> { viewModelFactory }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        changeViewModel.changeLiveData.observe(this, this)
        return AlertDialog.Builder(requireActivity())
                .setView(layoutInflater.inflate(R.layout.dialog_change_password, null))
                .setPositiveButton(android.R.string.ok) { _, _ -> checkChange() }
                .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
                .create()
    }

    private fun checkChange() = changeViewModel.checkChange(
            oldPassword = old_password_edit_text.text,
            newPassword = new_password_edit_text.text,
            repeatNewPassword = repeat_new_password_edit_text.text
    )

    override fun onChanged(changeResult: ChangeResult) {
        old_password_text_input_layout.error = null
        new_password_text_input_layout.error = null
        repeat_new_password_text_input_layout.error = null
        when (changeResult) {
            ChangeResult.Success -> dismiss()
            ChangeResult.OldEmptyPasswordError -> {
                old_password_text_input_layout.error = requireContext().getString(R.string.empty_password)
            }
            ChangeResult.NewEmptyPasswordError -> {
                new_password_text_input_layout.error = requireContext().getString(R.string.empty_password)
            }
            ChangeResult.PasswordsNoMatchError -> {
                repeat_new_password_text_input_layout.error = requireContext().getString(R.string.passwords_do_not_match)
            }
            ChangeResult.IncorrectPasswordError -> {
                old_password_text_input_layout.error = requireContext().getString(R.string.incorrect_password)
            }
            is ChangeResult.Error -> showError(changeResult.message)
        }
    }

    private fun showError(message: String?) = with(AlertDialog.Builder(requireContext())) {
        setTitle(android.R.string.dialog_alert_title)
        setMessage(message)
        setNeutralButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
    }.show()
}