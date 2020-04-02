package com.softartdev.noteroom.ui.main

import com.softartdev.noteroom.data.NoteUseCase
import com.softartdev.noteroom.ui.base.BaseViewModel
import com.softartdev.noteroom.ui.base.ResultFactory


class MainViewModel (
        private val noteUseCase: NoteUseCase
) : BaseViewModel<NoteListResult>() {

    init {
        updateNotes()
    }

    fun updateNotes() = launch {
        val notes = noteUseCase.getNotes()
        NoteListResult.Success(notes)
    }

    override val resultFactory: ResultFactory<NoteListResult> = NoteListResult.Factory()
}