package com.softartdev.noteroom.data

import com.softartdev.noteroom.database.Note
import kotlinx.coroutines.channels.Channel

interface NoteUseCase {

    val titleChannel: Channel<String>

    suspend fun getNotes(): List<Note>

    suspend fun createNote(title: String = "", text: String = ""): Long

    suspend fun saveNote(id: Long, title: String, text: String): Int

    suspend fun updateTitle(id: Long, title: String): Int

    suspend fun loadNote(noteId: Long): Note

    suspend fun deleteNote(id: Long): Int

    suspend fun isChanged(id: Long, title: String, text: String): Boolean

    suspend fun isEmpty(id: Long): Boolean

}
