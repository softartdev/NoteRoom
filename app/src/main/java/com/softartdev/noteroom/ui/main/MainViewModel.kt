package com.softartdev.noteroom.ui.main

import com.softartdev.noteroom.data.NoteUseCase
import com.softartdev.noteroom.database.Note
import com.softartdev.noteroom.ui.base.BaseViewModel
import kotlinx.coroutines.flow.map
import net.sqlcipher.database.SQLiteException


class MainViewModel (
        private val noteUseCase: NoteUseCase
) : BaseViewModel<NoteListResult>() {

    override val loadingResult: NoteListResult = NoteListResult.Loading

    fun updateNotes() = launch(
            flow = noteUseCase.getNotes().map { notes: List<Note> ->
                NoteListResult.Success(notes)
            })

    override fun errorResult(throwable: Throwable): NoteListResult = when (throwable) {
        is SQLiteException -> NoteListResult.NavMain
        else -> NoteListResult.Error(throwable.message)
    }
}