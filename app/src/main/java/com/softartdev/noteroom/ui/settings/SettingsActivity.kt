package com.softartdev.noteroom.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.softartdev.noteroom.R
import com.softartdev.noteroom.util.tintIcon

class SettingsActivity : AppCompatActivity(R.layout.settings_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}