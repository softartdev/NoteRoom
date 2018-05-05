package com.softartdev.noteroom.ui.storage

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button

import com.softartdev.noteroom.R

import de.jonasrottmann.realmbrowser.RealmBrowser
import io.realm.Realm

class StorageActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)

        val filesButton = findViewById<Button>(R.id.storage_files_button)
        filesButton.setOnClickListener(this)
        val tablesButton = findViewById<Button>(R.id.storage_tables_button)
        tablesButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.storage_files_button -> RealmBrowser.startRealmFilesActivity(this)
            R.id.storage_tables_button -> RealmBrowser.startRealmModelsActivity(this, Realm.getDefaultInstance().configuration)
        }
    }

}
