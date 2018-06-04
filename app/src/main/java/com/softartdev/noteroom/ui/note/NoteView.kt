package com.softartdev.noteroom.ui.note

import com.softartdev.noteroom.ui.base.MvpView

interface NoteView : MvpView {
    fun onLoadNote(title: String, text: String)
    fun onSaveNote(title: String)
    fun onEmptyNote()
    fun onDeleteNote()
    fun onCheckSaveChange()
    fun onNavBack()
}
