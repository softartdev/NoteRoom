package com.softartdev.noteroom.old

import android.text.Editable

import com.softartdev.noteroom.database.Note

interface DbStore {
    suspend fun getNotes(): List<Note>

    suspend fun isEncryption(): Boolean

    suspend fun createNote(title: String, text: String): Long

    suspend fun saveNote(id: Long, title: String, text: String): Int

    suspend fun updateTitle(id: Long, title: String)

    suspend fun loadNote(noteId: Long): Note

    suspend fun deleteNote(id: Long): Int

    suspend fun checkPass(pass: Editable): Boolean

    suspend fun changePass(oldPass: Editable?, newPass: Editable?)

    suspend fun isChanged(id: Long, title: String, text: String): Boolean

    suspend fun isEmpty(id: Long): Boolean
}
