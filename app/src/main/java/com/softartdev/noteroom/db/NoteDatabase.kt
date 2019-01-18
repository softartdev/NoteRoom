package com.softartdev.noteroom.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.softartdev.noteroom.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
