package com.softartdev.noteroom.model

sealed class NoteResult {
    object Loading: NoteResult()
    data class Created(val noteId: Long) : NoteResult()
    data class Loaded(val result: Note) : NoteResult()
    data class Saved(val title: String) : NoteResult()
    object Empty : NoteResult()
    object Deleted : NoteResult()
    object CheckSaveChange : NoteResult()
    object NavBack : NoteResult()
    data class Error(val message: String?) : NoteResult()
}