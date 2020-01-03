package com.softartdev.noteroom.ui.security

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatDialogFragment
import javax.inject.Inject

abstract class BaseDialogFragment(
        @LayoutRes private val dialogLayoutRes: Int
) : DaggerAppCompatDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog
            .Builder(requireActivity())
            .setView(dialogLayoutRes)
            .setPositiveButton(android.R.string.ok, null)
            .setNegativeButton(android.R.string.cancel, null)
            .create()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireDialog().setOnShowListener {
            val okButton = requireDialog().findViewById<Button>(android.R.id.button1)
            okButton.setOnClickListener { onOkClicked() }
        }
    }

    abstract fun onOkClicked()

    fun showError(message: String?): AlertDialog = with(AlertDialog.Builder(requireContext())) {
        setTitle(android.R.string.dialog_alert_title)
        setMessage(message)
        setNeutralButton(android.R.string.cancel, null)
    }.show()

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_FIRST_USER, null)
    }

    companion object {
        internal const val DIALOG_REQUEST_CODE = 378
    }
}