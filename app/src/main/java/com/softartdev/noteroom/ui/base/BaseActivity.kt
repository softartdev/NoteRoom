package com.softartdev.noteroom.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.softartdev.noteroom.R
import com.softartdev.noteroom.ui.settings.SettingsActivity
import com.softartdev.noteroom.util.PreferencesHelper
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity() {

    private val preferencesHelper by inject<PreferencesHelper>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide task snapshot if enable in settings
        if (preferencesHelper.hideScreenContentsEntry) {
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        R.id.action_settings -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}