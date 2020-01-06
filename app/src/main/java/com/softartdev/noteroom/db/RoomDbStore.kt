package com.softartdev.noteroom.db

import android.content.Context
import com.softartdev.noteroom.model.Note
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.*

class RoomDbStore(context: Context) : RoomDbRepository(context) {

    override val notes: Flowable<List<Note>>
        get() = noteDao.getNotes()

    override fun createNote(title: String, text: String): Single<Long> {
        val date = Date()
        val note = Note(0, title, text, date, date)
        return noteDao.insertNote(note)
    }

    override fun saveNote(id: Long, title: String, text: String): Single<Int> = noteDao
            .getNoteById(id)
            .flatMapSingle {
                noteDao.updateNote(note = it.copy(
                        title = title,
                        text = text,
                        dateModified = Date()
                ))
            }

    override fun updateTitle(id: Long, title: String): Completable = noteDao
            .getNoteById(id)
            .flatMapCompletable {
                noteDao.updateNote(note = it.copy(
                        title = title,
                        dateModified = Date()
                )).ignoreElement()
            }

    override fun loadNote(noteId: Long): Maybe<Note> = noteDao.getNoteById(noteId)

    override fun deleteNote(id: Long): Single<Int> = noteDao.deleteNoteById(id)

    override fun isChanged(id: Long, title: String, text: String): Single<Boolean> = noteDao.getNoteById(id)
            .map { it.title != title || it.text != text }
            .toSingle(false)

    override fun isEmpty(id: Long): Single<Boolean> = noteDao.getNoteById(id)
            .map { it.title.isEmpty() && it.text.isEmpty() }
            .toSingle(true)
}