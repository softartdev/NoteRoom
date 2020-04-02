package com.softartdev.noteroom.ui.main

import com.softartdev.noteroom.database.Note
import com.softartdev.noteroom.ui.base.ResultFactory
import net.sqlcipher.database.SQLiteException

sealed class NoteListResult{
    object Loading : NoteListResult()
    data class Success(val result: List<Note>) : NoteListResult()
    object NavMain : NoteListResult()
    data class Error(val error: String? = null) : NoteListResult()

    class Factory : ResultFactory<NoteListResult>() {
        override val loadingResult = Loading
        override fun errorResult(throwable: Throwable): NoteListResult = when (throwable) {
            is SQLiteException -> NavMain
            else -> Error(throwable.message)
        }
    }
}