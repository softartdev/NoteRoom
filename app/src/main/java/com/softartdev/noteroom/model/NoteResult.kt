package com.softartdev.noteroom.model

sealed class NoteResult {
    data class Success(val result: Note) : NoteResult()
    data class SaveNote(val title: String) : NoteResult()
    object EmptyNote : NoteResult()
    object DeleteNote : NoteResult()
    object CheckSaveChange : NoteResult()
    object NavBack : NoteResult()
}