package com.softartdev.noteroom.ui.title

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softartdev.noteroom.data.DataManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class EditTitleViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val editTitleLiveData = MutableLiveData<EditTitleResult>()

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

    private fun launch(
            block: suspend CoroutineScope.() -> EditTitleResult
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            editTitleLiveData.value = EditTitleResult.Loading
            val editTitleResult = try {
                block()
            } catch (throwable: Throwable) {
                Timber.e(throwable)
                EditTitleResult.Error(throwable.message)
            }
            editTitleLiveData.value = editTitleResult
        }
    }

}
