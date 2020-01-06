package com.softartdev.noteroom.data

import android.text.Editable
import com.softartdev.noteroom.db.DbStore
import com.softartdev.noteroom.model.Note
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class DataManager(private val dbStore: DbStore) {

    val titleSubject: Subject<String> by lazy { PublishSubject.create<String>() }

    fun notes(): Flowable<List<Note>> = dbStore.notes

    fun createNote(title: String = "", text: String = ""): Single<Long> = dbStore.createNote(title, text)

    fun saveNote(id: Long, title: String, text: String): Single<Int> = dbStore.saveNote(id, title, text)

    fun updateTitle(id: Long, title: String): Completable = dbStore.updateTitle(id, title)

    fun loadNote(noteId: Long): Maybe<Note> = dbStore.loadNote(noteId)

    fun deleteNote(id: Long): Single<Int> = dbStore.deleteNote(id)

    fun checkPass(pass: Editable): Single<Boolean> = dbStore.checkPass(pass)

    fun isEncryption(): Single<Boolean> = Single.just(dbStore.isEncryption)

    fun changePass(odlPass: Editable?, newPass: Editable?): Completable = Completable.fromCallable {
        dbStore.changePass(odlPass, newPass)
    }

    fun checkChanges(id: Long, title: String, text: String): Single<Boolean> = dbStore.isChanged(id, title, text)

    fun emptyNote(id: Long): Single<Boolean> = dbStore.isEmpty(id)
}
