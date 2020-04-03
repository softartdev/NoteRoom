package com.softartdev.noteroom.ui.main

import com.softartdev.noteroom.data.NoteUseCase
import com.softartdev.noteroom.ui.base.BaseViewModel
import net.sqlcipher.database.SQLiteException


class MainViewModel (
        private val noteUseCase: NoteUseCase
) : BaseViewModel<NoteListResult>() {

    override val loadingResult: NoteListResult = NoteListResult.Loading

    fun updateNotes() = launch {
        val notes = noteUseCase.getNotes()
        NoteListResult.Success(notes)
    }

    override fun errorResult(throwable: Throwable): NoteListResult = when (throwable) {
        is SQLiteException -> NoteListResult.NavMain
        else -> NoteListResult.Error(throwable.message)
    }
}