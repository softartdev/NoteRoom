package com.softartdev.noteroom.ui.note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.model.NoteResult
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class NoteViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val noteLiveData: MutableLiveData<NoteResult> = MutableLiveData()

    internal var noteId: Long = 0
        get() = when (field) {
            0L -> throw IllegalStateException()
            else -> field
        }

    private val compositeDisposable = CompositeDisposable()

    fun createNote() {
        compositeDisposable.add(dataManager.createNote()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = {
                    noteId = it
                    noteLiveData.value = NoteResult.Created(it)
                    Timber.d("Created note with id=$noteId")
                }, onError = Timber::e))
    }

    fun loadNote(id: Long) {
        compositeDisposable.add(dataManager.loadNote(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = {
                    noteId = it.id
                    noteLiveData.value = (NoteResult.Loaded(it))
                    Timber.d("Loaded note with id=$noteId")
                }, onError = Timber::e))
    }

    fun saveNote(title: String, text: String) {
        if (title.isEmpty() && text.isEmpty()) {
            noteLiveData.value = (NoteResult.Empty)
        } else compositeDisposable.add(dataManager.saveNote(noteId, title, text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = {
                    noteLiveData.value = (NoteResult.Saved(title))
                    Timber.d("Saved note with id=$noteId")
                }, onError = Timber::e))
    }

    fun deleteNote() {
        compositeDisposable.add(dataManager.deleteNote(noteId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = {
                    noteLiveData.value = (NoteResult.Deleted)
                    Timber.d("Deleted note with id=$noteId")
                }, onError = Timber::e))
    }

    fun checkSaveChange(title: String, text: String) {
        compositeDisposable.add(Single.zip(
                dataManager.checkChanges(noteId, title, text),
                dataManager.emptyNote(noteId),
                BiFunction<Boolean, Boolean, Pair<Boolean, Boolean>> { changed, empty -> Pair(changed, empty) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = { pair ->
                    val (changed, empty) = pair
                    when {
                        changed -> noteLiveData.value = (NoteResult.CheckSaveChange)
                        empty -> deleteNote()
                        else -> noteLiveData.value = (NoteResult.NavBack)
                    }
                }, onError = Timber::e))
    }

    fun saveNoteAndNavBack(title: String, text: String) {
        Timber.d("Save note with id=${noteId} before nav back")
        compositeDisposable.add(dataManager.saveNote(noteId, title, text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = {
                    noteLiveData.value = (NoteResult.NavBack)
                    Timber.d("Saved and nav back")
                }, onError = Timber::e))
    }

    fun doNotSaveAndNavBack() {
        compositeDisposable.add(dataManager.emptyNote(noteId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = { noteIsEmpty ->
                    if (noteIsEmpty) {
                        deleteNote()
                    } else {
                        noteLiveData.value = (NoteResult.NavBack)
                        Timber.d("Don't save and nav back")
                    }
                }, onError = Timber::e))
    }

    override fun onCleared() = compositeDisposable.clear()
}