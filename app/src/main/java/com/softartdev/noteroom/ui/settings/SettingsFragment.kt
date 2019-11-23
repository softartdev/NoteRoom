package com.softartdev.noteroom.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.preference.*
import com.softartdev.noteroom.R
import com.softartdev.noteroom.ui.base.BasePrefFragment
import com.softartdev.noteroom.ui.security.PassMediator
import com.softartdev.noteroom.ui.security.SecurityPresenter
import com.softartdev.noteroom.ui.security.SecurityView
import com.softartdev.noteroom.util.ThemeHelper
import com.softartdev.noteroom.util.tintIcon
import kotlinx.android.synthetic.main.dialog_change_password.view.*
import kotlinx.android.synthetic.main.dialog_password.view.*
import kotlinx.android.synthetic.main.dialog_set_password.view.*
import timber.log.Timber
import java.lang.IllegalArgumentException
import javax.inject.Inject

@SuppressLint("InflateParams")
class SettingsFragment : BasePrefFragment(), SecurityView, Preference.OnPreferenceChangeListener {

    @Inject lateinit var securityPresenter: SecurityPresenter
    private var securityPreferences: SwitchPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentComponent().inject(this)
        securityPresenter.attachView(this)
        securityPresenter.checkEncryption()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val themePreferenceCategory: PreferenceCategory? = findPreference(getString(R.string.theme))
        themePreferenceCategory?.tintIcon()

        val themePreference: ListPreference? = findPreference(getString(R.string.theme_key))
        themePreference?.onPreferenceChangeListener = this
        themePreference?.tintIcon()

        val securityPreferenceCategory: PreferenceCategory? = findPreference(getString(R.string.security))
        securityPreferenceCategory?.tintIcon()

        securityPreferences = findPreference(getString(R.string.security_key))
        securityPreferences?.onPreferenceChangeListener = this
        securityPreferences?.tintIcon()

        val passwordPreference: Preference? = findPreference(getString(R.string.password_key))
        passwordPreference?.setOnPreferenceClickListener {
            securityPresenter.changePassword()
            true
        }
        passwordPreference?.tintIcon()

        findPreference<CheckBoxPreference>(getString(R.string.hide_screen_contents_key))?.tintIcon()
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean = when (preference?.key) {
        getString(R.string.theme_key) -> {
            ThemeHelper.applyTheme(newValue as String, requireContext())
            true
        }
        getString(R.string.security_key) -> {
            securityPresenter.changeEncryption(newValue as Boolean)
            false
        }
        else -> throw IllegalArgumentException("Unknown preference key")
    }

    override fun showEncryptEnable(encryption: Boolean) = securityPreferences?.run {
        onPreferenceChangeListener = null
        isChecked = encryption
        onPreferenceChangeListener = this@SettingsFragment
    } ?: Timber.e("Security switch preference is null")

    override fun showPasswordDialog() {
        val dialogPasswordView = layoutInflater.inflate(R.layout.dialog_password, null)
        val alertDialog = createDialog(dialogPasswordView)
        dialogPositiveClickListener(alertDialog, View.OnClickListener {
            val pass = PassMediator(dialogPasswordView.enter_password_text_input_layout, dialogPasswordView.enter_password_edit_text)
            securityPresenter.enterPassCorrect(pass) { alertDialog.dismiss() }
        })
        alertDialog.show()
    }

    override fun showSetPasswordDialog() {
        val dialogPasswordView = layoutInflater.inflate(R.layout.dialog_set_password, null)
        val alertDialog = createDialog(dialogPasswordView)
        dialogPositiveClickListener(alertDialog, View.OnClickListener {
            val pass = PassMediator(dialogPasswordView.set_password_text_input_layout, dialogPasswordView.set_password_edit_text)
            val repeatPass = PassMediator(dialogPasswordView.repeat_set_password_text_input_layout, dialogPasswordView.repeat_set_password_edit_text)
            securityPresenter.setPassCorrect(pass, repeatPass) { alertDialog.dismiss() }
        })
        alertDialog.show()
    }

    override fun showChangePasswordDialog() {
        val dialogPasswordView = layoutInflater.inflate(R.layout.dialog_change_password, null)
        val alertDialog = createDialog(dialogPasswordView)
        dialogPositiveClickListener(alertDialog, View.OnClickListener {
            val oldPass = PassMediator(dialogPasswordView.old_password_text_input_layout, dialogPasswordView.old_password_edit_text)
            val newPass = PassMediator(dialogPasswordView.new_password_text_input_layout, dialogPasswordView.new_password_edit_text)
            val repeatNewPass = PassMediator(dialogPasswordView.repeat_new_password_text_input_layout, dialogPasswordView.repeat_new_password_edit_text)
            securityPresenter.changePassCorrect(oldPass, newPass, repeatNewPass) { alertDialog.dismiss() }
        })
        alertDialog.show()
    }

    private fun createDialog(dialogView: View?): AlertDialog = with(AlertDialog.Builder(requireContext())) {
        setView(dialogView)
        setPositiveButton(android.R.string.ok, null)
        setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
        create()
    }

    private fun dialogPositiveClickListener(
            alertDialog: AlertDialog,
            clickListener: View.OnClickListener
    ) = alertDialog.setOnShowListener {
        val okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        okButton.setOnClickListener(clickListener)
    }

    override fun showError(message: String?) = with(AlertDialog.Builder(requireContext())) {
        setTitle(android.R.string.dialog_alert_title)
        setMessage(message)
        setNeutralButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
        show(); Unit
    }

    override fun onDestroy() {
        super.onDestroy()
        securityPresenter.detachView()
    }
}