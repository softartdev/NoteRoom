package com.softartdev.noteroom.ui.note

import com.softartdev.noteroom.data.NoteUseCase
import com.softartdev.noteroom.database.Note
import kotlinx.coroutines.channels.Channel
import java.util.*

class NoteUseCaseStub : NoteUseCase {

    val notes: MutableList<Note> = mutableListOf()

    override val titleChannel: Channel<String> by lazy { return@lazy Channel<String>() }

    fun inflate() {
        val firstNote = Note(1, "first title", "first text", Date(), Date())
        val secondNote = Note(2, "second title", "second text", Date(), Date())
        val thirdNote = Note(3, "third title", "third text", Date(), Date())
        notes.addAll(listOf(firstNote, secondNote, thirdNote))
    }

    fun clear() = notes.clear()

    override suspend fun getNotes(): List<Note> = notes

    override suspend fun createNote(title: String, text: String): Long {
        val date = Date()
        val note = Note(notes.last().id.inc(), title, text, date, date)
        notes += note
        return note.id
    }

    override suspend fun saveNote(id: Long, title: String, text: String): Int {
        val index: Int = notes.indexOfFirst { foundNote -> foundNote.id == id }
        if (index != -1) {
            val note = notes[index].copy(
                    title = title,
                    text = text,
                    dateModified = Date()
            )
            notes.removeAt(index)
            notes.add(index, note)
        }
        return 1
    }

    override suspend fun updateTitle(id: Long, title: String): Int {
        val index: Int = notes.indexOfFirst { foundNote -> foundNote.id == id }
        if (index != -1) {
            val note = notes[index].copy(
                    title = title,
                    dateModified = Date()
            )
            notes.removeAt(index)
            notes.add(index, note)
        }
        return 1
    }

    override suspend fun loadNote(noteId: Long): Note = notes.first { note -> note.id == noteId }

    override suspend fun deleteNote(id: Long): Int {
        val index: Int = notes.indexOfFirst { foundNote -> foundNote.id == id }
        if (index != -1) {
            notes.removeAt(index)
        }
        return 1
    }

    override suspend fun isChanged(id: Long, title: String, text: String): Boolean {
        val note = loadNote(id)
        return note.title != title || note.text != text
    }

    override suspend fun isEmpty(id: Long): Boolean {
        val note = loadNote(id)
        return note.title.isEmpty() && note.text.isEmpty()
    }

}
