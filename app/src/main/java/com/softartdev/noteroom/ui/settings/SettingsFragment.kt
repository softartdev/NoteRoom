package com.softartdev.noteroom.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.preference.*
import com.softartdev.noteroom.R
import com.softartdev.noteroom.model.SecurityResult
import com.softartdev.noteroom.ui.base.BasePrefFragment
import com.softartdev.noteroom.ui.security.SecurityViewModel
import com.softartdev.noteroom.ui.security.change.ChangePasswordDialog
import com.softartdev.noteroom.ui.security.confirm.ConfirmPasswordDialog
import com.softartdev.noteroom.ui.security.enter.EnterPasswordDialog
import com.softartdev.noteroom.util.ThemeHelper
import com.softartdev.noteroom.util.tintIcon
import timber.log.Timber

@SuppressLint("InflateParams")
class SettingsFragment : BasePrefFragment(), Preference.OnPreferenceChangeListener, Observer<SecurityResult> {

    private val securityViewModel by viewModels<SecurityViewModel> { viewModelFactory }
    private var securityPreferences: SwitchPreference? = null

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
            securityViewModel.changePassword()
            true
        }
        passwordPreference?.tintIcon()

        findPreference<CheckBoxPreference>(getString(R.string.hide_screen_contents_key))?.tintIcon()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        securityViewModel.securityLiveData.observe(this, this)
        securityViewModel.checkEncryption()
    }

    override fun onChanged(securityResult: SecurityResult) = when (securityResult) {
        is SecurityResult.EncryptEnable -> showEncryptEnable(securityResult.encryption)
        is SecurityResult.PasswordDialog -> showPasswordDialog()
        is SecurityResult.SetPasswordDialog -> showSetPasswordDialog()
        is SecurityResult.ChangePasswordDialog -> showChangePasswordDialog()
        is SecurityResult.Error -> showError(securityResult.message)
    }

    private fun showEncryptEnable(encryption: Boolean) = securityPreferences?.run {
        onPreferenceChangeListener = null
        isChecked = encryption
        onPreferenceChangeListener = this@SettingsFragment
    } ?: Timber.e("Security switch preference is null")

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean = when (preference?.key) {
        getString(R.string.theme_key) -> {
            ThemeHelper.applyTheme(newValue as String, requireContext())
            true
        }
        getString(R.string.security_key) -> {
            securityViewModel.changeEncryption(newValue as Boolean)
            false
        }
        else -> throw IllegalArgumentException("Unknown preference key")
    }

    private fun showPasswordDialog() {
        val dialog = EnterPasswordDialog()
        dialog.show(parentFragmentManager, null)
    }

    private fun showSetPasswordDialog() {
        val dialog = ConfirmPasswordDialog()
        dialog.show(parentFragmentManager, null)
    }

    private fun showChangePasswordDialog() {
        val dialog = ChangePasswordDialog()
        dialog.show(parentFragmentManager, null)
    }

    private fun showError(message: String?) = with(AlertDialog.Builder(requireContext())) {
        setTitle(android.R.string.dialog_alert_title)
        setMessage(message)
        setNeutralButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
        show(); Unit
    }

}