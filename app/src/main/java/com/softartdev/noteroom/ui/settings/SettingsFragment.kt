package com.softartdev.noteroom.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.preference.*
import com.softartdev.noteroom.R
import com.softartdev.noteroom.ui.base.BaseDialogFragment
import com.softartdev.noteroom.ui.settings.security.change.ChangePasswordDialog
import com.softartdev.noteroom.ui.settings.security.confirm.ConfirmPasswordDialog
import com.softartdev.noteroom.ui.settings.security.enter.EnterPasswordDialog
import com.softartdev.noteroom.util.ThemeHelper
import com.softartdev.noteroom.util.tintIcon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.scope.viewModel
import timber.log.Timber

@SuppressLint("InflateParams")
class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener, Observer<SecurityResult> {

    private val settingsViewModel by lifecycleScope.viewModel<SettingsViewModel>(this)
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
            settingsViewModel.changePassword()
            true
        }
        passwordPreference?.tintIcon()

        findPreference<CheckBoxPreference>(getString(R.string.hide_screen_contents_key))?.tintIcon()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsViewModel.viewModelScope.launch(Dispatchers.Main) { settingsViewModel.flow.collect { onChanged(it) } }
        settingsViewModel.checkEncryption()
    }

    override fun onChanged(securityResult: SecurityResult) = when (securityResult) {
        is SecurityResult.EncryptEnable -> showEncryptEnable(securityResult.encryption)
        is SecurityResult.PasswordDialog -> showDialogFragment(EnterPasswordDialog())
        is SecurityResult.SetPasswordDialog -> showDialogFragment(ConfirmPasswordDialog())
        is SecurityResult.ChangePasswordDialog -> showDialogFragment(ChangePasswordDialog())
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
            settingsViewModel.changeEncryption(newValue as Boolean)
            false
        }
        else -> throw IllegalArgumentException("Unknown preference key")
    }

    private fun showDialogFragment(dialogFragment: DialogFragment) {
        dialogFragment.setTargetFragment(this, BaseDialogFragment.DIALOG_REQUEST_CODE)
        dialogFragment.show(parentFragmentManager, "PASSWORD_DIALOG_TAG")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) = when (requestCode) {
        BaseDialogFragment.DIALOG_REQUEST_CODE -> settingsViewModel.checkEncryption()
        else -> super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showError(message: String?) {
        AlertDialog.Builder(requireContext())
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage(message)
                .setNeutralButton(android.R.string.cancel, null)
                .show()
    }

}