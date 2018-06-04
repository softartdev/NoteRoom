package com.softartdev.noteroom.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import com.commonsware.cwac.saferoom.SQLCipherUtils
import com.commonsware.cwac.saferoom.SafeHelperFactory

abstract class RoomDbRepository(private val context: Context) : DbStore {

    private val dbName = "notes.db"

    private var noteDatabase: NoteDatabase = db()

    val noteDao: NoteDao
        get() = noteDatabase.noteDao()

    private fun db(passphrase: Editable = SpannableStringBuilder()): NoteDatabase = Room
            .databaseBuilder(context, NoteDatabase::class.java, dbName)
            .openHelperFactory(SafeHelperFactory.fromUser(passphrase))
            .build()

    override fun isEncryption(): Boolean = when(SQLCipherUtils.getDatabaseState(context, dbName)) {
        SQLCipherUtils.State.ENCRYPTED -> true
        SQLCipherUtils.State.UNENCRYPTED -> false
        SQLCipherUtils.State.DOES_NOT_EXIST -> false
        else -> throw RuntimeException("Cannot check encryption state")
    }

    override fun checkPass(pass: Editable): Boolean =
            try {
                noteDatabase.close()
                val passphrase = Editable.Factory.getInstance().newEditable(pass) // threadsafe
                noteDatabase = db(passphrase)
                noteDao.getNotes()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }

    override fun changePass(oldPass: Editable?, newPass: Editable?) {
        if (isEncryption) {
            if (newPass.isNullOrEmpty()) {
                val originalFile = context.getDatabasePath(dbName)

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

                passphrase?.let { noteDatabase = db(it) }
            }
        } else {
            val passphrase = Editable.Factory.getInstance().newEditable(newPass) // threadsafe

            noteDatabase.close()
            SQLCipherUtils.encrypt(context, dbName, passphrase)

            passphrase?.let { noteDatabase = db(it) }
        }
    }
}