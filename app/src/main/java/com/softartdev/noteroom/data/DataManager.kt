package com.softartdev.noteroom.data

import android.text.Editable
import com.softartdev.noteroom.db.DbStore
import com.softartdev.noteroom.model.Note
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager @Inject
internal constructor(private val dbStore: DbStore) {

    fun notes(): Single<List<Note>> = Single.fromCallable {
        dbStore.notes
    }

    fun createNote(title: String, text: String): Single<Long> = Single.fromCallable {
        dbStore.createNote(title, text)
    }

    fun saveNote(id: Long, title: String, text: String): Single<Unit> = Single.fromCallable {
        dbStore.saveNote(id, title, text)
    }

    fun loadNote(noteId: Long): Single<Note> = Single.fromCallable {
        dbStore.loadNote(noteId)
    }

    fun deleteNote(id: Long): Single<Unit> = Single.fromCallable {
        dbStore.deleteNote(id)
    }

    fun checkPass(pass: Editable): Single<Boolean> = Single.fromCallable {
        dbStore.checkPass(pass)
    }

    fun isEncryption(): Single<Boolean> = Single.fromCallable {
        dbStore.isEncryption
    }

    fun changePass(odlPass: Editable?, newPass: Editable?): Single<Unit> = Single.fromCallable {
        dbStore.changePass(odlPass, newPass)
    }

    fun checkChanges(id: Long, title: String, text: String): Single<Boolean> = Single.fromCallable {
        dbStore.isChanged(id, title, text)
    }

    fun emptyNote(id: Long): Single<Boolean> = Single.fromCallable {
        dbStore.isEmpty(id)
    }
}
