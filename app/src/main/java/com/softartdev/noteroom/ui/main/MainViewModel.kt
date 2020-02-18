package com.softartdev.noteroom.ui.main

import com.softartdev.noteroom.data.NoteUseCase
import com.softartdev.noteroom.ui.base.BaseViewModel
import net.sqlcipher.database.SQLiteException
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val noteUseCase: NoteUseCase
) : BaseViewModel<NoteListResult>() {

    init {
        updateNotes()
    }

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