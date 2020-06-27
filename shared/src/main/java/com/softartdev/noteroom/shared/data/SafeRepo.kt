package com.softartdev.noteroom.shared.data

import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import androidx.room.Room
import com.commonsware.cwac.saferoom.SQLCipherUtils
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.softartdev.noteroom.shared.database.NoteDao
import com.softartdev.noteroom.shared.database.NoteDatabase

class SafeRepo(
        private val context: Context
) {

    @Volatile
    private var noteDatabase: NoteDatabase? = buildDatabaseInstanceIfNeed()

    val databaseState: SQLCipherUtils.State
        get() = SQLCipherUtils.getDatabaseState(context, DB_NAME)

    val noteDao: NoteDao
        get() = noteDatabase?.noteDao() ?: throw SafeSQLiteException("DB is null")

    var relaunchFlowEmitter: (() -> Unit)? = null

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

    fun decrypt(oldPass: Editable) {
        val originalFile = context.getDatabasePath(DB_NAME)

        val oldCopy = SpannableStringBuilder(oldPass) // threadsafe
        val passphrase = CharArray(oldCopy.length)
        oldCopy.getChars(0, oldCopy.length, passphrase, 0)

        closeDatabase()
        SQLCipherUtils.decrypt(context, originalFile, passphrase)

        buildDatabaseInstanceIfNeed()
    }

    fun rekey(oldPass: Editable, newPass: Editable) {
        val passphrase = SpannableStringBuilder(newPass) // threadsafe

        val supportSQLiteDatabase = buildDatabaseInstanceIfNeed(oldPass).openHelper.writableDatabase
        SafeHelperFactory.rekey(supportSQLiteDatabase, passphrase)

        buildDatabaseInstanceIfNeed(passphrase)
    }

    fun encrypt(newPass: Editable) {
        val passphrase = SpannableStringBuilder(newPass) // threadsafe

        closeDatabase()
        SQLCipherUtils.encrypt(context, DB_NAME, passphrase)

        buildDatabaseInstanceIfNeed(passphrase)
    }

    fun closeDatabase() = synchronized(this) {
        noteDatabase?.close()
        noteDatabase = null
    }

    companion object {
        const val DB_NAME = "notes.db"
    }
}