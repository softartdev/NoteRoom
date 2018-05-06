package com.softartdev.noteroom.db

import android.content.Context
import com.softartdev.noteroom.model.Note
import java.util.*

class RoomDbStore(context: Context) : RoomDbRepository(context) {

    override fun getNotes(): List<Note> {
        return noteDao.getNotes()
    }

    override fun createNote(): Long {
        val date = Date()
        val note = Note(0, "", "", date, date)
        return noteDao.insertNote(note)
    }

    override fun saveNote(id: Long, title: String, text: String) {
        noteDao.getNoteById(id)?.let {
            it.title = title
            it.text = text
            it.dateModified = Date()
            noteDao.updateNote(it)
        }
    }

    override fun loadNote(noteId: Long): Note? {
        return noteDao.getNoteById(noteId)
    }

    override fun deleteNote(id: Long) {
        val result: Int = noteDao.deleteNoteById(id)
        if (result != 1) throw RuntimeException("Note with id=$id wasn't delete^ result=$result")
    }

    override fun isChanged(id: Long, title: String, text: String): Boolean {
        val note = noteDao.getNoteById(id)
        return note?.title != title || note.text != text
    }

    override fun isEmpty(id: Long): Boolean {
        val note = noteDao.getNoteById(id)
        return note?.title.isNullOrEmpty() && note?.text.isNullOrEmpty()
    }
}