package com.softartdev.noteroom.ui.main

import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.ui.base.BaseViewModel
import net.sqlcipher.database.SQLiteException
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val dataManager: DataManager
) : BaseViewModel<NoteListResult>() {

    init {
        updateNotes()
    }

    override val loadingResult: NoteListResult = NoteListResult.Loading

    fun updateNotes() = launch {
        val notes = dataManager.notes()
        NoteListResult.Success(notes)
    }

    override fun errorResult(throwable: Throwable): NoteListResult = when (throwable) {
        is SQLiteException -> NoteListResult.NavMain
        else -> NoteListResult.Error(throwable.message)
    }
}