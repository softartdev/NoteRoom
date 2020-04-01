package com.softartdev.noteroom.data

import android.text.Editable
import android.text.SpannableStringBuilder
import com.commonsware.cwac.saferoom.SQLCipherUtils

class CryptUseCase(
        private val safeRepo: SafeRepo
) {

    fun dbIsEncrypted(): Boolean = when (safeRepo.databaseState) {
        SQLCipherUtils.State.ENCRYPTED -> true
        SQLCipherUtils.State.UNENCRYPTED -> false
        SQLCipherUtils.State.DOES_NOT_EXIST -> false
    }

    suspend fun checkPassword(pass: Editable): Boolean = try {
        safeRepo.closeDatabase()
        val passphrase = SpannableStringBuilder(pass) // threadsafe
        safeRepo.buildDatabaseInstanceIfNeed(passphrase)
        safeRepo.noteDao.getNotes()//TODO remove if no need (after tests for sign in)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    fun changePassword(oldPass: Editable?, newPass: Editable?) {
        if (dbIsEncrypted()) {
            requireNotNull(oldPass)
            if (newPass.isNullOrEmpty()) {
                safeRepo.decrypt(oldPass)
            } else {
                safeRepo.rekey(oldPass, newPass)
            }
        } else {
            requireNotNull(newPass)
            safeRepo.encrypt(newPass)
        }
    }
}