package com.softartdev.noteroom.data

import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import androidx.room.Room
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.softartdev.noteroom.database.NoteDao
import com.softartdev.noteroom.database.NoteDatabase

class SafeRepo(
        private val context: Context
) {

    @Volatile
    private var noteDatabase: NoteDatabase? = null

    val noteDao: NoteDao
        get() = noteDatabase?.noteDao() ?: throw SafeSQLiteException("DB is null")

    fun buildDatabaseInstanceIfNeed(
            passphrase: Editable = SpannableStringBuilder()
    ): NoteDatabase = synchronized(this) {
        var instance = noteDatabase
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context, NoteDatabase::class.java, DB_NAME)
                    .openHelperFactory(SafeHelperFactory.fromUser(passphrase))
                    .build()
            noteDatabase = instance
        }
        return instance
    }

    fun closeDatabase() = synchronized(this) {
        noteDatabase?.close()
        noteDatabase = null
    }

    companion object {
        const val DB_NAME = "notes.db"
    }
}