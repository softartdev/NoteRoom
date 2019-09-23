package com.softartdev.noteroom.ui.settings

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.softartdev.noteroom.R
import com.softartdev.noteroom.util.ThemeHelper

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val themePreference = findPreference<ListPreference>(getString(R.string.theme_key))
        themePreference?.setOnPreferenceChangeListener { _: Preference, newValue: Any ->
            val themeOption: String = newValue as String
            ThemeHelper.applyTheme(themeOption, requireContext())
            true
        }
    }
}