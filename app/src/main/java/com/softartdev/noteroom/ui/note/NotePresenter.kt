package com.softartdev.noteroom.ui.note

import com.crashlytics.android.Crashlytics
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.di.ConfigPersistent
import com.softartdev.noteroom.model.Note
import com.softartdev.noteroom.ui.base.BasePresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@ConfigPersistent
class NotePresenter @Inject constructor(
        private val dataManager: DataManager
) : BasePresenter<NoteView>() {

    private var mNote: Note? = null

    fun createNote() {
        checkViewAttached()
        addDisposable(dataManager.createNote("", "")
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
        checkViewAttached()
        addDisposable(dataManager.loadNote(noteId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ note ->
                    Timber.d("Loaded: $note")
                    mNote = note
                    mvpView?.onLoadNote(note.title, note.text)
                }, { throwable ->
                    Crashlytics.logException(throwable)
                    throwable.printStackTrace()
                }))
    }

    fun saveNote(title: String, text: String) {
        checkViewAttached()
        if (title.isEmpty() && text.isEmpty()) {
            mvpView?.onEmptyNote()
        } else {
            val saveSingle = mNote?.id?.let { dataManager.saveNote(it, title, text) } ?: dataManager.createNote(title, text)
            addDisposable(saveSingle
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mvpView?.onSaveNote(title)
                        Timber.d("Saved: $mNote")
                    }, { throwable ->
                        Crashlytics.logException(throwable)
                        throwable.printStackTrace()
                    }))
        }
    }

    fun deleteNote() {
        checkViewAttached()
        addDisposable(dataManager.deleteNote(mNote!!.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Note deleted")
                }, { throwable ->
                    Crashlytics.logException(throwable)
                    throwable.printStackTrace()
                }))
        mNote = null
        mvpView?.onDeleteNote()
        mvpView?.onNavBack()
    }

    fun checkSaveChange(title: String, text: String) {
        checkViewAttached()
        mNote?.let {
            addDisposable(Single.zip(
                    dataManager.checkChanges(it.id, title, text),
                    dataManager.emptyNote(it.id),
                    BiFunction<Boolean, Boolean, Pair<Boolean, Boolean>> { changed, empty -> Pair(changed, empty) })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ pair ->
                        val changed = pair.first
                        val empty = pair.second
                        if (changed) {
                            mvpView?.onCheckSaveChange()
                        } else {
                            if (empty) {
                                deleteNote()
                            }
                            mvpView?.onNavBack()
                        }
                    }, { throwable ->
                        Crashlytics.logException(throwable)
                        throwable.printStackTrace()
                    }))
        } ?: mvpView?.onCheckSaveChange()
    }
}
