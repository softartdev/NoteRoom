package com.softartdev.noteroom.data

import android.text.Editable
import com.softartdev.noteroom.db.DbStore
import com.softartdev.noteroom.model.Note
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

class DataManager(private val dbStore: DbStore) {

    fun notes(): Flowable<List<Note>> = dbStore.notes

    fun createNote(title: String = "", text: String = ""): Single<Long> = dbStore.createNote(title, text)

    fun saveNote(id: Long, title: String, text: String): Single<Int> = dbStore.saveNote(id, title, text)

    fun loadNote(noteId: Long): Maybe<Note> = dbStore.loadNote(noteId)

    fun deleteNote(id: Long): Single<Int> = dbStore.deleteNote(id)

    fun checkPass(pass: Editable): Single<Boolean> = dbStore.checkPass(pass)

    fun isEncryption(): Single<Boolean> = Single.fromCallable {
        dbStore.isEncryption
    }

    fun changePass(odlPass: Editable?, newPass: Editable?): Single<Unit> = Single.fromCallable {
        dbStore.changePass(odlPass, newPass)
    }

    fun checkChanges(id: Long, title: String, text: String): Single<Boolean> = dbStore.isChanged(id, title, text)

    fun emptyNote(id: Long): Single<Boolean> = dbStore.isEmpty(id)
}
