package com.softartdev.noteroom.ui.main

import com.softartdev.noteroom.model.Note
import com.softartdev.noteroom.ui.base.MvpView

interface MainView : MvpView {
    fun onAddNote()
    fun onUpdateNotes(notes: List<Note>)
}
