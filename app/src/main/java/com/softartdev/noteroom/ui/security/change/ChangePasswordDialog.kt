package com.softartdev.noteroom.ui.security.change

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.softartdev.noteroom.R
import com.softartdev.noteroom.ui.security.SecurityViewModel
import dagger.android.support.DaggerAppCompatDialogFragment
import javax.inject.Inject

class ChangePasswordDialog : DaggerAppCompatDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val securityViewModel by viewModels<SecurityViewModel> { viewModelFactory }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
                .setView(layoutInflater.inflate(R.layout.dialog_change_password, null))
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
                .create()
    }
}