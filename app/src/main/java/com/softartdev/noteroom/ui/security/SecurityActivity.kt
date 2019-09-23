package com.softartdev.noteroom.ui.security

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.textfield.TextInputLayout
import com.softartdev.noteroom.R
import com.softartdev.noteroom.ui.base.BaseActivity
import com.softartdev.noteroom.util.tintIcon
import com.softartdev.noteroom.util.tintLeftDrawable
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security)
        activityComponent().inject(this)
        securityPresenter.attachView(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        enable_encryption_switch.tintLeftDrawable(R.drawable.ic_lock_black_24dp)
        securityPresenter.checkEncryption()

        set_password_button.tintLeftDrawable(R.drawable.ic_password_black_24dp)
        set_password_button.setOnClickListener { securityPresenter.changePassword() }
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
                securityPresenter.enterPassCorrect(pass) { alertDialog.dismiss() }
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
                securityPresenter.setPassCorrect(pass, repeatPass) { alertDialog.dismiss() }
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
                securityPresenter.changePassCorrect(oldPass, newPass, repeatNewPass) { alertDialog.dismiss() }
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_oss, menu)
        menu.findItem(R.id.action_oss).tintIcon(this)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_oss -> {
            startActivity(Intent(this, OssLicensesMenuActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        securityPresenter.detachView()
        super.onDestroy()
    }
}
