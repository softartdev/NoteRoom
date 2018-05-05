package com.softartdev.noteroom.ui.note

import android.text.TextUtils
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.di.ConfigPersistent
import com.softartdev.noteroom.model.Note
import com.softartdev.noteroom.ui.base.BasePresenter
import timber.log.Timber
import javax.inject.Inject

@ConfigPersistent
class NotePresenter @Inject
constructor(private val dataManager: DataManager) : BasePresenter<NoteView>() {
    private var mNote: Note? = null

    fun createNote() {
        addDisposable(dataManager.createNote()
                .subscribe({note ->
                    mNote = note
                }, {it.printStackTrace() }))
    }

    fun loadNote(noteId: Long) {
        addDisposable(dataManager.loadNote(noteId)
                .subscribe({note ->
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
                    .subscribe(
                            { mvpView?.onSaveNote(title) },
                            { it.printStackTrace() }))
        }
    }

    fun deleteNote() {
        addDisposable(dataManager.deleteNote(mNote!!.id)
                .subscribe(
                        { Timber.d("Note deleted") }
                        , { it.printStackTrace() }))
        mNote = null
        mvpView?.onDeleteNote()
        mvpView?.onNavBack()
    }

    fun checkSaveChange(title: String, text: String) {
        val changed = dataManager.checkChanges(mNote!!.id, title, text)
        if (changed) {
            mvpView?.onCheckSaveChange()
            return
        }
        val empty = dataManager.emptyNote(mNote!!.id)
        if (empty) {
            deleteNote()
        }
        mvpView?.onNavBack()
    }
}
