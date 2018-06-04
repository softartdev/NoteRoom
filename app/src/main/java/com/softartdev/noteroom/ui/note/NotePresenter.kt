package com.softartdev.noteroom.ui.note

import android.text.TextUtils
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
class NotePresenter @Inject
constructor(private val dataManager: DataManager) : BasePresenter<NoteView>() {
    private var mNote: Note? = null

    fun createNote() {
        addDisposable(dataManager.createNote()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ noteId ->
                    Timber.d("Created: $noteId")
                    loadNote(noteId)
                }, { it.printStackTrace() }))
    }

    fun loadNote(noteId: Long) {
        addDisposable(dataManager.loadNote(noteId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ note ->
                    Timber.d("Loaded: $note")
                    mNote = note
                    mvpView?.onLoadNote(note.title, note.text)
                }, { it.printStackTrace() }))
    }

    fun saveNote(title: String, text: String) {
        if (mNote == null) {
            createNote()
        }

        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(text)) {
            mvpView?.onEmptyNote()
        } else {
            addDisposable(dataManager.saveNote(mNote!!.id, title, text)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mvpView?.onSaveNote(title)
                        Timber.d("Saved: $mNote")
                    }, {
                        it.printStackTrace()
                    }))
        }
    }

    fun deleteNote() {
        addDisposable(dataManager.deleteNote(mNote!!.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { Timber.d("Note deleted") }
                        , { it.printStackTrace() }))
        mNote = null
        mvpView?.onDeleteNote()
        mvpView?.onNavBack()
    }

    fun checkSaveChange(title: String, text: String) {
        addDisposable(Single.zip(
                dataManager.checkChanges(mNote!!.id, title, text),
                dataManager.emptyNote(mNote!!.id),
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
                }, { it.printStackTrace() }))
    }
}
