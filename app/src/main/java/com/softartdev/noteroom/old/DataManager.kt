package com.softartdev.noteroom.old

import android.text.Editable
import com.softartdev.noteroom.database.Note
import kotlinx.coroutines.channels.Channel

class DataManager(private val dbStore: DbStore) {

    val titleChannel: Channel<String> by lazy { Channel<String>() }

    suspend fun notes(): List<Note> = dbStore.getNotes()

    suspend fun createNote(title: String = "", text: String = ""): Long = dbStore.createNote(title, text)

    suspend fun saveNote(id: Long, title: String, text: String): Int = dbStore.saveNote(id, title, text)

    suspend fun updateTitle(id: Long, title: String) = dbStore.updateTitle(id, title)

    suspend fun loadNote(noteId: Long): Note = dbStore.loadNote(noteId)

    suspend fun deleteNote(id: Long): Int = dbStore.deleteNote(id)

    suspend fun checkPass(pass: Editable): Boolean = dbStore.checkPass(pass)

    suspend fun isEncryption(): Boolean = dbStore.isEncryption()

    suspend fun changePass(odlPass: Editable?, newPass: Editable?) = dbStore.changePass(odlPass, newPass)

    suspend fun checkChanges(id: Long, title: String, text: String): Boolean = dbStore.isChanged(id, title, text)

    suspend fun emptyNote(id: Long): Boolean = dbStore.isEmpty(id)
}
