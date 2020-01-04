package com.softartdev.noteroom.model

sealed class NoteListResult{
    data class Success(val result: List<Note>) : NoteListResult()
    data class Error(val error: String? = null) : NoteListResult()
    object Loading : NoteListResult()
    object NavMain : NoteListResult()
}