package com.softartdev.noteroom.ui.note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.model.NoteResult
import com.softartdev.noteroom.util.createTitle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class NoteViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val noteLiveData: MutableLiveData<NoteResult> = MutableLiveData()

    private var noteId: Long = 0
        get() = when (field) {
            0L -> throw IllegalStateException()
            else -> field
        }

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

    private fun launch(
            block: suspend CoroutineScope.() -> NoteResult
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            onResult(noteResult = NoteResult.Loading)
            val noteResult: NoteResult = try {
                block()
            } catch (e: Throwable) {
                Timber.e(e)
                NoteResult.Error(e.message)
            }
            onResult(noteResult)
        }
    }

    private suspend fun subscribeToEditTitle() = viewModelScope.launch(Dispatchers.IO) {
        val noteResult = try {
            val title = dataManager.titleChannel.receive()
            NoteResult.TitleUpdated(title)
        } catch (e: Throwable) {
            Timber.e(e)
            NoteResult.Error(e.message)
        }
        onResult(noteResult)
    }

    private suspend fun onResult(noteResult: NoteResult) = withContext(Dispatchers.Main) {
        noteLiveData.value = noteResult
    }

}