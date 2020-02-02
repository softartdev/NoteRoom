package com.softartdev.noteroom.ui.title

import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.ui.base.BaseViewModel
import javax.inject.Inject

class EditTitleViewModel @Inject constructor(
        private val dataManager: DataManager
) : BaseViewModel<EditTitleResult>() {

    override val loadingResult: EditTitleResult = EditTitleResult.Loading

    fun loadTitle(noteId: Long) = launch {
        val note = dataManager.loadNote(noteId)
        EditTitleResult.Loaded(note.title)
    }

    fun editTitle(id: Long, newTitle: String) = launch {
        val (noteId, noteTitle) = id to newTitle.trim()
        when {
            noteTitle.isEmpty() -> EditTitleResult.EmptyTitleError
            else -> {
                dataManager.updateTitle(noteId, noteTitle)
                dataManager.titleChannel.send(noteTitle)
                EditTitleResult.Success
            }
        }
    }

    override fun errorResult(throwable: Throwable): EditTitleResult = EditTitleResult.Error(throwable.message)
}
