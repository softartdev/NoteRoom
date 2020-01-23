package com.softartdev.noteroom.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.model.NoteListResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.sqlcipher.database.SQLiteException
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val notesLiveData: MutableLiveData<NoteListResult> = MutableLiveData()

    init {
        updateNotes()
    }

    fun updateNotes() = viewModelScope.launch(Dispatchers.IO) {
        onResult(NoteListResult.Loading)
        val noteListResult = try {
            val notes = dataManager.notes()
            NoteListResult.Success(notes)
        } catch (throwable: Throwable) {
            Timber.e(throwable)
            when (throwable) {
                is SQLiteException -> NoteListResult.NavMain
                else -> NoteListResult.Error(throwable.message)
            }
        }
        onResult(noteListResult)
    }

    private suspend fun onResult(noteListResult: NoteListResult) = withContext(Dispatchers.Main) {
        notesLiveData.value = noteListResult
    }
}