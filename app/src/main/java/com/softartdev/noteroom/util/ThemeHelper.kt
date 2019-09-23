package com.softartdev.noteroom.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.BuildCompat
import com.softartdev.noteroom.R

object ThemeHelper {

    fun applyTheme(themePref: String, context: Context) = AppCompatDelegate.setDefaultNightMode(when (themePref) {
        context.getString(R.string.light_theme_entry) -> AppCompatDelegate.MODE_NIGHT_NO
        context.getString(R.string.dark_theme_entry) -> AppCompatDelegate.MODE_NIGHT_YES
        else -> if (BuildCompat.isAtLeastQ()) AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM else AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
    })
}
