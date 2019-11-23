package com.softartdev.noteroom.ui.note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crashlytics.android.Crashlytics
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.model.Note
import com.softartdev.noteroom.model.NoteResult
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class NoteViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val noteLiveData: MutableLiveData<NoteResult> = MutableLiveData()

    private val note: Note?
        get() = (noteLiveData.value as? NoteResult.Success)?.result

    private val compositeDisposable = CompositeDisposable()

    fun createNote() {
        compositeDisposable.add(dataManager.createNote("", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ noteId ->
                    Timber.d("Created: $noteId")
                    loadNote(noteId)
                }, { throwable ->
                    Crashlytics.logException(throwable)
                    throwable.printStackTrace()
                }))
    }

    fun loadNote(noteId: Long) {
        compositeDisposable.add(dataManager.loadNote(noteId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ note ->
                    Timber.d("Loaded: $note")
                    noteLiveData.postValue(NoteResult.Success(note))
                }, { throwable ->
                    Crashlytics.logException(throwable)
                    throwable.printStackTrace()
                }))
    }

    fun saveNote(title: String, text: String) {
        if (title.isEmpty() && text.isEmpty()) {
            noteLiveData.postValue(NoteResult.EmptyNote)
        } else {
            val saveSingle = note?.id?.let {
                dataManager.saveNote(it, title, text)
            } ?: dataManager.createNote(title, text)
            compositeDisposable.add(saveSingle
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        noteLiveData.postValue(NoteResult.SaveNote(title))
                        Timber.d("Saved: $note")
                    }, { throwable ->
                        Crashlytics.logException(throwable)
                        throwable.printStackTrace()
                    }))
        }
    }

    fun deleteNote() {
        compositeDisposable.add(dataManager.deleteNote(note!!.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Note deleted")
                }, { throwable ->
                    Crashlytics.logException(throwable)
                    throwable.printStackTrace()
                }))
        noteLiveData.postValue(NoteResult.DeleteNote)
        noteLiveData.postValue(NoteResult.NavBack)
    }

    fun checkSaveChange(title: String, text: String) {
        note?.let {
            compositeDisposable.add(Single.zip(
                    dataManager.checkChanges(it.id, title, text),
                    dataManager.emptyNote(it.id),
                    BiFunction<Boolean, Boolean, Pair<Boolean, Boolean>> { changed, empty -> Pair(changed, empty) })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ pair ->
                        val changed = pair.first
                        val empty = pair.second
                        if (changed) {
                            noteLiveData.postValue(NoteResult.CheckSaveChange)
                        } else {
                            if (empty) {
                                deleteNote()
                            }
                            noteLiveData.postValue(NoteResult.NavBack)
                        }
                    }, { throwable ->
                        Crashlytics.logException(throwable)
                        throwable.printStackTrace()
                    }))
        } ?: noteLiveData.postValue(NoteResult.CheckSaveChange)
    }

    override fun onCleared() = compositeDisposable.clear()
}