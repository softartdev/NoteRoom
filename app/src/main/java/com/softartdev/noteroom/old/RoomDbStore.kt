package com.softartdev.noteroom.old

import android.content.Context
import com.softartdev.noteroom.database.Note
import java.util.*

class RoomDbStore(context: Context) : RoomDbRepository(context) {

    override suspend fun getNotes(): List<Note> = noteDao.getNotes()

    override suspend fun createNote(title: String, text: String): Long {
        val date = Date()
        val note = Note(0, title, text, date, date)
        return noteDao.insertNote(note)
    }

    override suspend fun saveNote(id: Long, title: String, text: String): Int {
        val note = noteDao.getNoteById(id).copy(
                title = title,
                text = text,
                dateModified = Date()
        )
        return noteDao.updateNote(note)
    }

    override suspend fun updateTitle(id: Long, title: String) {
        val note = noteDao.getNoteById(id).copy(
                title = title,
                dateModified = Date()
        )
        noteDao.updateNote(note)
    }

    override suspend fun loadNote(noteId: Long): Note = noteDao.getNoteById(noteId)

    override suspend fun deleteNote(id: Long): Int = noteDao.deleteNoteById(id)

    override suspend fun isChanged(id: Long, title: String, text: String): Boolean {
        val note = noteDao.getNoteById(id)
        return note.title != title || note.text != text
    }

    override suspend fun isEmpty(id: Long): Boolean {
        val note = noteDao.getNoteById(id)
        return note.title.isEmpty() && note.text.isEmpty()
    }
}