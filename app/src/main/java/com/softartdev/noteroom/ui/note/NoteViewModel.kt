package com.softartdev.noteroom.ui.note

import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.ui.base.BaseViewModel
import com.softartdev.noteroom.util.createTitle
import timber.log.Timber
import javax.inject.Inject

class NoteViewModel @Inject constructor(
        private val dataManager: DataManager
) : BaseViewModel<NoteResult>() {

    private var noteId: Long = 0
        get() = when (field) {
            0L -> throw IllegalStateException()
            else -> field
        }

    override val loadingResult: NoteResult = NoteResult.Loading

    fun createNote() = launch {
        val note = dataManager.createNote()
        noteId = note
        Timber.d("Created note with id=$noteId")
        NoteResult.Created(note)
    }

    fun loadNote(id: Long) = launch {
        val note = dataManager.loadNote(id)
        noteId = note.id
        Timber.d("Loaded note with id=$noteId")
        NoteResult.Loaded(note)
    }

    fun saveNote(title: String?, text: String) = launch {
        if (title.isNullOrEmpty() && text.isEmpty()) {
            NoteResult.Empty
        } else {
            val noteTitle = title ?: createTitle(text)
            dataManager.saveNote(noteId, noteTitle, text)
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
        val changed = dataManager.checkChanges(noteId, noteTitle, text)
        val empty = dataManager.emptyNote(noteId)
        when {
            changed -> NoteResult.CheckSaveChange
            empty -> deleteNoteForResult()
            else -> NoteResult.NavBack
        }
    }

    fun saveNoteAndNavBack(title: String?, text: String) = launch {
        val noteTitle = title ?: createTitle(text)
        dataManager.saveNote(noteId, noteTitle, text)
        Timber.d("Saved and nav back")
        NoteResult.NavBack
    }

    fun doNotSaveAndNavBack() = launch {
        val noteIsEmpty = dataManager.emptyNote(noteId)
        if (noteIsEmpty) {
            deleteNoteForResult()
        } else {
            Timber.d("Don't save and nav back")
            NoteResult.NavBack
        }
    }

    private suspend fun deleteNoteForResult(): NoteResult {
        dataManager.deleteNote(noteId)
        Timber.d("Deleted note with id=$noteId")
        return NoteResult.Deleted
    }

    private suspend fun subscribeToEditTitle() = launchForReceive {
        val title = dataManager.titleChannel.receive()
        NoteResult.TitleUpdated(title)
    }

    override fun errorResult(throwable: Throwable): NoteResult = NoteResult.Error(throwable.message)
}