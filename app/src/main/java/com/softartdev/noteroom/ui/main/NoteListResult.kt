package com.softartdev.noteroom.ui.main

import com.softartdev.noteroom.shared.database.Note

sealed class NoteListResult{
    object Loading : NoteListResult()
    data class Success(val result: List<Note>) : NoteListResult()
    object NavMain : NoteListResult()
    data class Error(val error: String? = null) : NoteListResult()
}