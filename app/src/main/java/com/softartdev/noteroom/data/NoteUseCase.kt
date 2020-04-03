package com.softartdev.noteroom.data

import com.softartdev.noteroom.database.Note
import kotlinx.coroutines.channels.Channel
import java.util.*

class NoteUseCase(
        private val safeRepo: SafeRepo
) {

    val titleChannel: Channel<String> by lazy { return@lazy Channel<String>() }
    
    suspend fun getNotes(): List<Note> = safeRepo.noteDao.getNotes()

    suspend fun createNote(title: String = "", text: String = ""): Long {
        val date = Date()
        val note = Note(0, title, text, date, date)
        return safeRepo.noteDao.insertNote(note)
    }

    suspend fun saveNote(id: Long, title: String, text: String): Int {
        val note = safeRepo.noteDao.getNoteById(id).copy(
                title = title,
                text = text,
                dateModified = Date()
        )
        return safeRepo.noteDao.updateNote(note)
    }

    suspend fun updateTitle(id: Long, title: String): Int {
        val note = safeRepo.noteDao.getNoteById(id).copy(
                title = title,
                dateModified = Date()
        )
        return safeRepo.noteDao.updateNote(note)
    }

    suspend fun loadNote(noteId: Long): Note = safeRepo.noteDao.getNoteById(noteId)

    suspend fun deleteNote(id: Long): Int = safeRepo.noteDao.deleteNoteById(id)

    suspend fun isChanged(id: Long, title: String, text: String): Boolean {
        val note = safeRepo.noteDao.getNoteById(id)
        return note.title != title || note.text != text
    }

    suspend fun isEmpty(id: Long): Boolean {
        val note = safeRepo.noteDao.getNoteById(id)
        return note.title.isEmpty() && note.text.isEmpty()
    }

}