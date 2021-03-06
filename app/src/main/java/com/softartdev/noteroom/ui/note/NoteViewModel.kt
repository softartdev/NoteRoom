package com.softartdev.noteroom.ui.note

import androidx.annotation.VisibleForTesting
import com.softartdev.noteroom.shared.data.NoteUseCase
import com.softartdev.noteroom.ui.base.BaseViewModel
import com.softartdev.noteroom.util.createTitle
import timber.log.Timber


class NoteViewModel (
        private val noteUseCase: NoteUseCase
) : BaseViewModel<NoteResult>() {

    private var noteId: Long = 0
        get() = when (field) {
            0L -> throw IllegalStateException("Note doesn't loaded")
            else -> field
        }

    override val loadingResult: NoteResult = NoteResult.Loading

    fun createNote() = launch {
        val note = noteUseCase.createNote()
        noteId = note
        Timber.d("Created note with id=$noteId")
        NoteResult.Created(note)
    }

    fun loadNote(id: Long) = launch {
        val note = noteUseCase.loadNote(id)
        noteId = note.id
        Timber.d("Loaded note with id=$noteId")
        NoteResult.Loaded(note)
    }

    fun saveNote(title: String?, text: String) = launch {
        if (title.isNullOrEmpty() && text.isEmpty()) {
            NoteResult.Empty
        } else {
            val noteTitle = title ?: createTitle(text)
            noteUseCase.saveNote(noteId, noteTitle, text)
            Timber.d("Saved note with id=$noteId")
            NoteResult.Saved(noteTitle)
        }
    }

    fun editTitle() = launch {
        subscribeToEditTitle()
        NoteResult.NavEditTitle(noteId)
    }

    fun deleteNote() = launch { deleteNoteForResult() }

    fun checkSaveChange(title: String?, text: String) = launch {
        val noteTitle = title ?: createTitle(text)
        val changed = noteUseCase.isChanged(noteId, noteTitle, text)
        val empty = noteUseCase.isEmpty(noteId)
        when {
            changed -> NoteResult.CheckSaveChange
            empty -> deleteNoteForResult()
            else -> NoteResult.NavBack
        }
    }

    fun saveNoteAndNavBack(title: String?, text: String) = launch {
        val noteTitle = title ?: createTitle(text)
        noteUseCase.saveNote(noteId, noteTitle, text)
        Timber.d("Saved and nav back")
        NoteResult.NavBack
    }

    fun doNotSaveAndNavBack() = launch {
        val noteIsEmpty = noteUseCase.isEmpty(noteId)
        if (noteIsEmpty) {
            deleteNoteForResult()
        } else {
            Timber.d("Don't save and nav back")
            NoteResult.NavBack
        }
    }

    private suspend fun deleteNoteForResult(): NoteResult {
        noteUseCase.deleteNote(noteId)
        Timber.d("Deleted note with id=$noteId")
        return NoteResult.Deleted
    }

    private suspend fun subscribeToEditTitle() = launch(useIdling = false) {
        val title = noteUseCase.titleChannel.receive()
        NoteResult.TitleUpdated(title)
    }

    override fun errorResult(throwable: Throwable): NoteResult = NoteResult.Error(throwable.message)

    @VisibleForTesting
    fun setIdForTest(id: Long) {
        noteId = id
    }
}