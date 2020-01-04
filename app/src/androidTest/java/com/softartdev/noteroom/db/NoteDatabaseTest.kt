package com.softartdev.noteroom.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.softartdev.noteroom.db.RoomDbRepository.Companion.DB_NAME
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class NoteDatabaseTest {

    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: NoteDatabase
    private lateinit var noteDao: NoteDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.deleteDatabase(DB_NAME)
        db = Room.databaseBuilder(context, NoteDatabase::class.java, DB_NAME).build()
        noteDao = db.noteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun noteDao() {
        noteDao.getNotes()
                .test()
                .assertValue(emptyList())
                .assertNoErrors()
                .assertNotComplete()
    }
}