package com.softartdev.noteroom.data

import android.content.Context
import android.text.Editable
import com.commonsware.cwac.saferoom.SQLCipherUtils
import com.commonsware.cwac.saferoom.SafeHelperFactory

class CryptUseCase(
        private val context: Context,
        private val safeRepo: SafeRepo
) {

    fun dbIsEncrypted(): Boolean = when (SQLCipherUtils.getDatabaseState(context, SafeRepo.DB_NAME)) {
        SQLCipherUtils.State.ENCRYPTED -> true
        SQLCipherUtils.State.UNENCRYPTED -> false
        SQLCipherUtils.State.DOES_NOT_EXIST -> false
        else -> throw RuntimeException("Cannot check encryption state")
    }

    suspend fun checkPassword(pass: Editable): Boolean = try {
        safeRepo.closeDatabase()
        val passphrase = Editable.Factory.getInstance().newEditable(pass) // threadsafe
        safeRepo.buildDatabaseInstanceIfNeed(passphrase)
        safeRepo.noteDao.getNotes()//TODO remove if no need (after tests for sign in)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    fun changePass(oldPass: Editable?, newPass: Editable?) {
        if (dbIsEncrypted()) {
            if (newPass.isNullOrEmpty()) {
                val originalFile = context.getDatabasePath(SafeRepo.DB_NAME)

                val oldCopy = Editable.Factory.getInstance().newEditable(oldPass) // threadsafe
                val passphrase = CharArray(oldCopy.length)
                oldCopy?.getChars(0, oldCopy.length, passphrase, 0)

                safeRepo.closeDatabase()
                SQLCipherUtils.decrypt(context, originalFile, passphrase)

                safeRepo.buildDatabaseInstanceIfNeed()
            } else {
                val passphrase = Editable.Factory.getInstance().newEditable(newPass) // threadsafe

                val noteDatabase = safeRepo.buildDatabaseInstanceIfNeed()
                val supportSQLiteDatabase = noteDatabase.openHelper.writableDatabase
                SafeHelperFactory.rekey(supportSQLiteDatabase, passphrase)

                safeRepo.buildDatabaseInstanceIfNeed(passphrase)
            }
        } else {
            val passphrase = Editable.Factory.getInstance().newEditable(newPass) // threadsafe

            safeRepo.closeDatabase()
            SQLCipherUtils.encrypt(context, SafeRepo.DB_NAME, passphrase)

            safeRepo.buildDatabaseInstanceIfNeed(passphrase)
        }
    }
}