package com.softartdev.noteroom.ui.main

import com.softartdev.noteroom.model.Note
import com.softartdev.noteroom.ui.base.MvpView

interface MainView : MvpView {
    fun onUpdateNotes(noteList: List<Note>)
    fun showProgress(show: Boolean)
    fun showEmpty()
    fun navSignIn()
    fun showError(error: Throwable)
}
