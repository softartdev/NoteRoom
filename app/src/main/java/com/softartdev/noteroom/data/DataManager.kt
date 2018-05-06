package com.softartdev.noteroom.data

import android.support.annotation.Nullable
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

    fun createNote(): Single<Long> = Single.fromCallable {
        dbStore.createNote()
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

    fun checkPass(pass: String?): Boolean {
        return dbStore.checkPass(pass)
    }

    fun isEncryption(): Boolean {
        return dbStore.isEncryption
    }

    fun changePass(@Nullable odlPass: String?, @Nullable newPass: String?): Single<Unit> = Single.fromCallable {
        dbStore.changePass(odlPass, newPass)
    }

    fun checkChanges(id: Long, title: String, text: String): Single<Boolean> = Single.fromCallable {
        dbStore.isChanged(id, title, text)
    }

    fun emptyNote(id: Long): Single<Boolean> = Single.fromCallable {
        dbStore.isEmpty(id)
    }
}
