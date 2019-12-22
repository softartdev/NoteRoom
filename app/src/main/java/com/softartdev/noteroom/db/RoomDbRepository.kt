package com.softartdev.noteroom.db

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Room
import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import com.commonsware.cwac.saferoom.SQLCipherUtils
import com.commonsware.cwac.saferoom.SafeHelperFactory
import io.reactivex.Single

abstract class RoomDbRepository(private val context: Context) : DbStore {

    private var noteDatabase: NoteDatabase = db()

    val noteDao: NoteDao
        get() = noteDatabase.noteDao()

    private fun db(passphrase: Editable = SpannableStringBuilder()): NoteDatabase = Room
            .databaseBuilder(context, NoteDatabase::class.java, DB_NAME)
            .openHelperFactory(SafeHelperFactory.fromUser(passphrase))
            .build()

    override val isEncryption: Boolean
        get() = when (SQLCipherUtils.getDatabaseState(context, DB_NAME)) {
            SQLCipherUtils.State.ENCRYPTED -> true
            SQLCipherUtils.State.UNENCRYPTED -> false
            SQLCipherUtils.State.DOES_NOT_EXIST -> false
            else -> throw RuntimeException("Cannot check encryption state")
        }

    override fun checkPass(pass: Editable): Single<Boolean> = try {
        noteDatabase.close()
        val passphrase = Editable.Factory.getInstance().newEditable(pass) // threadsafe
        noteDatabase = db(passphrase)
        noteDao.getNotes()
                .singleOrError()
                .map { true }
                .onErrorReturn { false }
    } catch (e: Exception) {
        e.printStackTrace()
        Single.just(false)
    }

    override fun changePass(oldPass: Editable?, newPass: Editable?) {
        if (isEncryption) {
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

                passphrase?.let { noteDatabase = db(it) }
            }
        } else {
            val passphrase = Editable.Factory.getInstance().newEditable(newPass) // threadsafe

            noteDatabase.close()
            SQLCipherUtils.encrypt(context, DB_NAME, passphrase)

            passphrase?.let { noteDatabase = db(it) }
        }
    }

    companion object {
        const val DB_NAME = "notes.db"
    }
}