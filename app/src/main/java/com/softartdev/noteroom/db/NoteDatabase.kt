package com.softartdev.noteroom.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.softartdev.noteroom.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
