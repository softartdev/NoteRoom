package com.softartdev.noteroom.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softartdev.noteroom.model.Note
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*


@RunWith(AndroidJUnit4::class)
class NoteEntityReadWriteTest {

    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var noteDao: NoteDao
    private lateinit var db: NoteDatabase

    private val note = Note(
            id = 0,
            title = "Test title",
            text = "Test text",
            dateCreated = Date(),
            dateModified = Date()
    )
    private val expId = note.id + 1

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.deleteDatabase(RoomDbRepository.DB_NAME)
        db = Room.databaseBuilder(context, NoteDatabase::class.java, RoomDbRepository.DB_NAME).build()
        noteDao = db.noteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeNoteAndReadById() {
        noteDao.insertNote(note)
                .test()
                .assertValue(expId)
        val exp = note.copy(id = expId)
        noteDao.getNoteById(expId)
                .test()
                .assertValue(exp)
    }

    @Test
    @Throws(Exception::class)
    fun writeNoteAndReadInList() {
        noteDao.insertNote(note)
                .test()
                .assertValue(expId)
        val exp = note.copy(id = expId)
        noteDao.getNotes()
                .test()
                .assertValue(listOf(exp))
    }

}