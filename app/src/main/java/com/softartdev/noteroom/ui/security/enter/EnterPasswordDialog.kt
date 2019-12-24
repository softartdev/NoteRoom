package com.softartdev.noteroom.ui.security.enter

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.softartdev.noteroom.R
import dagger.android.support.DaggerAppCompatDialogFragment
import kotlinx.android.synthetic.main.dialog_password.*
import javax.inject.Inject

class EnterPasswordDialog : DaggerAppCompatDialogFragment(), Observer<EnterResult> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val enterViewModel by viewModels<EnterViewModel> { viewModelFactory }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        enterViewModel.enterLiveData.observe(this, this)
        return AlertDialog.Builder(requireActivity())
                .setView(layoutInflater.inflate(R.layout.dialog_password, null))
                .setPositiveButton(android.R.string.ok) { _, _ -> enterCheck() }
                .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
                .create()
    }

    private fun enterCheck() = enterViewModel.enterCheck(
            password = enter_password_edit_text.text
    )

    override fun onChanged(enterResult: EnterResult) {
        enter_password_text_input_layout.error = null
        when (enterResult) {
            EnterResult.Success -> dismiss()
            is EnterResult.EmptyPasswordError -> {
                enter_password_text_input_layout.error = requireContext().getString(R.string.empty_password)
            }
            is EnterResult.IncorrectPasswordError -> {
                enter_password_text_input_layout.error = requireContext().getString(R.string.incorrect_password)
            }
            is EnterResult.Error -> showError(enterResult.message)
        }
    }

    private fun showError(message: String?) = with(AlertDialog.Builder(requireContext())) {
        setTitle(android.R.string.dialog_alert_title)
        setMessage(message)
        setNeutralButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
    }.show()
}