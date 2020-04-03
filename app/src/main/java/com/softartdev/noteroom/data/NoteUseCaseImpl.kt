package com.softartdev.noteroom.data

import com.softartdev.noteroom.database.Note
import kotlinx.coroutines.channels.Channel
import java.util.*

class NoteUseCaseImpl (
        private val safeRepo: SafeRepo
) : NoteUseCase {

    override val titleChannel: Channel<String> by lazy { return@lazy Channel<String>() }
    
    override suspend fun getNotes(): List<Note> = safeRepo.noteDao.getNotes()

    override suspend fun createNote(title: String, text: String): Long {
        val date = Date()
        val note = Note(0, title, text, date, date)
        return safeRepo.noteDao.insertNote(note)
    }

    override suspend fun saveNote(id: Long, title: String, text: String): Int {
        val note = safeRepo.noteDao.getNoteById(id).copy(
                title = title,
                text = text,
                dateModified = Date()
        )
        return safeRepo.noteDao.updateNote(note)
    }

    override suspend fun updateTitle(id: Long, title: String): Int {
        val note = safeRepo.noteDao.getNoteById(id).copy(
                title = title,
                dateModified = Date()
        )
        return safeRepo.noteDao.updateNote(note)
    }

    override suspend fun loadNote(noteId: Long): Note = safeRepo.noteDao.getNoteById(noteId)

    override suspend fun deleteNote(id: Long): Int = safeRepo.noteDao.deleteNoteById(id)

    override suspend fun isChanged(id: Long, title: String, text: String): Boolean {
        val note = safeRepo.noteDao.getNoteById(id)
        return note.title != title || note.text != text
    }

    override suspend fun isEmpty(id: Long): Boolean {
        val note = safeRepo.noteDao.getNoteById(id)
        return note.title.isEmpty() && note.text.isEmpty()
    }

}