package com.softartdev.noteroom.db

import androidx.room.*
import com.softartdev.noteroom.model.Note
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface NoteDao {

    /**
     * Select all notes from the notes table.
     *
     * @return all notes.
     */
    @Query("SELECT * FROM note")
    fun getNotes(): Single<List<Note>>

    /**
     * Select a note by id.
     *
     * @param noteId the note id.
     * @return the note with noteId.
     */
    @Query("SELECT * FROM note WHERE id = :noteId")
    fun getNoteById(noteId: Long): Maybe<Note>

    /**
     * Insert a note in the database. If the note already exists, replace it.
     *
     * @param note the note to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note): Single<Long>

    /**
     * Update a note.
     *
     * @param note note to be updated
     * @return the number of notes updated. This should always be 1.
     */
    @Update
    fun updateNote(note: Note): Single<Int>

    /**
     * Delete a note by id.
     *
     * @return the number of notes deleted. This should always be 1.
     */
    @Query("DELETE FROM note WHERE id = :noteId")
    fun deleteNoteById(noteId: Long): Single<Int>

    /**
     * Delete all notes.
     */
    @Query("DELETE FROM note")
    fun deleteNotes(): Completable

}