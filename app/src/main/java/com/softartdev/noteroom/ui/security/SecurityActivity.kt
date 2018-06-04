package com.softartdev.noteroom.ui.security

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDelegate
import android.text.Editable
import android.widget.CompoundButton
import android.widget.EditText
import com.softartdev.noteroom.R
import com.softartdev.noteroom.ui.base.BaseActivity
import io.github.tonnyl.spark.Spark
import kotlinx.android.synthetic.main.activity_security.*
import kotlinx.android.synthetic.main.dialog_change_password.view.*
import kotlinx.android.synthetic.main.dialog_password.view.*
import kotlinx.android.synthetic.main.dialog_set_password.view.*
import javax.inject.Inject

class SecurityActivity : BaseActivity(), SecurityView, CompoundButton.OnCheckedChangeListener {

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    @Inject lateinit var securityPresenter: SecurityPresenter

    private lateinit var securitySpark: Spark

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security)
        activityComponent().inject(this)
        securityPresenter.attachView(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        securitySpark = Spark.Builder()
                .setView(security_layout)
                .setAnimList(Spark.ANIM_RED_PURPLE)
                .build()

        enable_encryption_switch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp, 0, 0, 0)
        securityPresenter.checkEncryption()

        set_password_button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_black_24dp, 0, 0, 0)
        set_password_button.setOnClickListener { securityPresenter.changePassword() }
    }

    override fun onResume() {
        super.onResume()
        securitySpark.startAnimation()
    }

    override fun onPause() {
        super.onPause()
        securitySpark.stopAnimation()
    }

    override fun showEncryptEnable(encryption: Boolean) {
        enable_encryption_switch.setOnCheckedChangeListener(null)
        enable_encryption_switch.isChecked = encryption
        enable_encryption_switch.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) =
            securityPresenter.changeEncryption(isChecked)

    override fun showPasswordDialog() {
        @SuppressLint("InflateParams") val dialogPasswordView = layoutInflater.inflate(R.layout.dialog_password, null)
        val alertDialog = with(AlertDialog.Builder(this)) {
            setView(dialogPasswordView)
            setPositiveButton(android.R.string.ok, null)
            setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
            create()
        }
        alertDialog.setOnShowListener {
            val okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.setOnClickListener {
                val pass = PassMediator(dialogPasswordView.enter_password_text_input_layout, dialogPasswordView.enter_password_edit_text)
                securityPresenter.enterPassCorrect(pass, { alertDialog.dismiss() })
            }
        }
        alertDialog.show()
    }

    override fun showSetPasswordDialog() {
        @SuppressLint("InflateParams") val dialogPasswordView = layoutInflater.inflate(R.layout.dialog_set_password, null)
        val alertDialog = with(AlertDialog.Builder(this)) {
            setView(dialogPasswordView)
            setPositiveButton(android.R.string.ok, null)
            setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
            create()
        }
        alertDialog.setOnShowListener {
            val okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.setOnClickListener {
                val pass = PassMediator(dialogPasswordView.set_password_text_input_layout, dialogPasswordView.set_password_edit_text)
                val repeatPass = PassMediator(dialogPasswordView.repeat_set_password_text_input_layout, dialogPasswordView.repeat_set_password_edit_text)
                securityPresenter.setPassCorrect(pass, repeatPass, { alertDialog.dismiss() })
            }
        }
        alertDialog.show()
    }

    override fun showChangePasswordDialog() {
        @SuppressLint("InflateParams") val dialogPasswordView = layoutInflater.inflate(R.layout.dialog_change_password, null)
        val alertDialog = with(AlertDialog.Builder(this)) {
            setView(dialogPasswordView)
            setPositiveButton(android.R.string.ok, null)
            setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
            create()
        }
        alertDialog.setOnShowListener {
            val okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.setOnClickListener {
                val oldPass = PassMediator(dialogPasswordView.old_password_text_input_layout, dialogPasswordView.old_password_edit_text)
                val newPass = PassMediator(dialogPasswordView.new_password_text_input_layout, dialogPasswordView.new_password_edit_text)
                val repeatNewPass = PassMediator(dialogPasswordView.repeat_new_password_text_input_layout, dialogPasswordView.repeat_new_password_edit_text)
                securityPresenter.changePassCorrect(oldPass, newPass, repeatNewPass, { alertDialog.dismiss() })
            }
        }
        alertDialog.show()
    }

    private inner class PassMediator internal constructor(internal var mTextInputLayout: TextInputLayout, internal var mEditText: EditText) : SecurityView.DialogDirector {

        override val textString: Editable
            get() = mEditText.text

        override fun showIncorrectPasswordError() {
            mTextInputLayout.error = getString(R.string.incorrect_password)
        }

        override fun showEmptyPasswordError() {
            mTextInputLayout.error = getString(R.string.empty_password)
        }

        override fun showPasswordsNoMatchError() {
            mTextInputLayout.error = getString(R.string.passwords_do_not_match)
        }

        override fun hideError() {
            mTextInputLayout.error = null
        }
    }

    override fun showError(message: String?) {
        with(AlertDialog.Builder(this)) {
            setTitle(android.R.string.dialog_alert_title)
            setMessage(message)
            setNeutralButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
            show()
        }
    }

    override fun onDestroy() {
        securityPresenter.detachView()
        super.onDestroy()
    }
}
