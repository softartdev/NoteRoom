package com.softartdev.noteroom.db

import android.arch.persistence.room.Room
import android.content.Context

abstract class RoomDbRepository(private val context: Context) : DbStore {

    private val noteDatabase: NoteDatabase by lazy {
        Room.databaseBuilder(context, NoteDatabase::class.java, "notes.db").build()
    }

    val noteDao: NoteDao = noteDatabase.noteDao()

    override fun isEncryption(): Boolean {
        //TODO
        return false
    }

    override fun checkPass(pass: String?): Boolean {
        //TODO
        return false
    }

    override fun changePass(odlPass: String?, newPass: String?) {
        //TODO
    }
}