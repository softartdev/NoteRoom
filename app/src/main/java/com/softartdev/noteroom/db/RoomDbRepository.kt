package com.softartdev.noteroom.db

import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.commonsware.cwac.saferoom.SQLCipherUtils
import com.commonsware.cwac.saferoom.SafeHelperFactory

abstract class RoomDbRepository(private val context: Context) : DbStore {

    private var noteDatabase: NoteDatabase = db()

    val noteDao: NoteDao
        get() = noteDatabase.noteDao()

    private fun db(passphrase: Editable = SpannableStringBuilder()): NoteDatabase = Room
            .databaseBuilder(context, NoteDatabase::class.java, DB_NAME)
            .openHelperFactory(SafeHelperFactory.fromUser(passphrase,
                    SafeHelperFactory.POST_KEY_SQL_MIGRATE))//TODO skip the second and subsequent times; all users need update to version 3.0 or above
            .build()

    override suspend fun isEncryption(): Boolean = when (SQLCipherUtils.getDatabaseState(context, DB_NAME)) {
        SQLCipherUtils.State.ENCRYPTED -> true
        SQLCipherUtils.State.UNENCRYPTED -> false
        SQLCipherUtils.State.DOES_NOT_EXIST -> false
        else -> throw RuntimeException("Cannot check encryption state")
    }

    override suspend fun checkPass(pass: Editable): Boolean = try {
        noteDatabase.close()
        val passphrase = Editable.Factory.getInstance().newEditable(pass) // threadsafe
        noteDatabase = db(passphrase)
        noteDao.getNotes()
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    override suspend fun changePass(oldPass: Editable?, newPass: Editable?) = if (isEncryption()) {
        if (newPass.isNullOrEmpty()) {
            val originalFile = context.getDatabasePath(DB_NAME)

            val oldCopy = Editable.Factory.getInstance().newEditable(oldPass) // threadsafe
            val passphrase = CharArray(oldCopy.length)
            oldCopy?.getChars(0, oldCopy.length, passphrase, 0)

            noteDatabase.close()
            SQLCipherUtils.decrypt(context, originalFile, passphrase)

            noteDatabase = db()
        } else {
            val passphrase = Editable.Factory.getInstance().newEditable(newPass) // threadsafe

            val supportSQLiteDatabase: SupportSQLiteDatabase = noteDatabase.openHelper.writableDatabase
            SafeHelperFactory.rekey(supportSQLiteDatabase, passphrase)

            noteDatabase = db(passphrase)
        }
    } else {
        val passphrase = Editable.Factory.getInstance().newEditable(newPass) // threadsafe

        noteDatabase.close()
        SQLCipherUtils.encrypt(context, DB_NAME, passphrase)

        noteDatabase = db(passphrase)
    }

    companion object {
        const val DB_NAME = "notes.db"
    }
}