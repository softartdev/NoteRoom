package com.softartdev.noteroom.db

import android.text.Editable

import com.softartdev.noteroom.model.Note

import io.reactivex.Maybe
import io.reactivex.Single

interface DbStore {
    val notes: Single<List<Note>>

    val isEncryption: Boolean

    fun createNote(title: String, text: String): Single<Long>

    fun saveNote(id: Long, title: String, text: String): Single<Int>

    fun loadNote(noteId: Long): Maybe<Note>

    fun deleteNote(id: Long): Single<Int>

    fun checkPass(pass: Editable): Single<Boolean>

    fun changePass(oldPass: Editable?, newPass: Editable?)

    fun isChanged(id: Long, title: String, text: String): Single<Boolean>

    fun isEmpty(id: Long): Single<Boolean>
}