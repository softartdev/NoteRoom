package com.softartdev.noteroom.ui.main

import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.di.ConfigPersistent
import com.softartdev.noteroom.ui.base.BasePresenter
import javax.inject.Inject

@ConfigPersistent
class MainPresenter @Inject
constructor(private val dataManager: DataManager) : BasePresenter<MainView>() {

    fun updateNotes() {
        checkViewAttached()
        addDisposable(dataManager.notes()
                .subscribe(
                        {notes -> mvpView?.onUpdateNotes(notes) }
                        , { it.printStackTrace() }))
    }

    fun addNote() {
        checkViewAttached()
        mvpView!!.onAddNote()
    }
}
