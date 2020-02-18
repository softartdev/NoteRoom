package com.softartdev.noteroom.old

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softartdev.noteroom.data.SafeRepo.Companion.DB_NAME
import com.softartdev.noteroom.database.NoteDao
import com.softartdev.noteroom.database.NoteDatabase
import com.softartdev.noteroom.database.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*


@RunWith(AndroidJUnit4::class)
@UseExperimental(ExperimentalCoroutinesApi::class)
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
        db = Room.databaseBuilder(context, NoteDatabase::class.java, DB_NAME).build()
        noteDao = db.noteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeNoteAndReadById() = runBlockingTest {
        val actId = noteDao.insertNote(note)
        assertEquals(expId, actId)
        val exp = note.copy(id = expId)
        val act = noteDao.getNoteById(expId)
        assertEquals(exp, act)
    }

    @Test
    @Throws(Exception::class)
    fun writeNoteAndReadInList() = runBlockingTest {
        val actId = noteDao.insertNote(note)
        assertEquals(expId, actId)
        val exp = listOf(note.copy(id = expId))
        val act = noteDao.getNotes()
        assertEquals(exp, act)
    }

}