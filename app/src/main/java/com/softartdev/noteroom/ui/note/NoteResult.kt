package com.softartdev.noteroom.ui.note

import com.softartdev.noteroom.database.Note
import com.softartdev.noteroom.ui.base.ResultFactory

sealed class NoteResult {
    object Loading : NoteResult()
    data class Created(val noteId: Long) : NoteResult()
    data class Loaded(val result: Note) : NoteResult()
    data class Saved(val title: String) : NoteResult()
    data class NavEditTitle(val noteId: Long) : NoteResult()
    data class TitleUpdated(val title: String) : NoteResult()
    object Empty : NoteResult()
    object Deleted : NoteResult()
    object CheckSaveChange : NoteResult()
    object NavBack : NoteResult()
    data class Error(val message: String?) : NoteResult()

    class Factory : ResultFactory<NoteResult>() {
        override val loadingResult = Loading
        override fun errorResult(throwable: Throwable) = Error(throwable.message)
    }
}