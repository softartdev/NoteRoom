package com.softartdev.noteroom.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.model.NoteListResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import net.sqlcipher.database.SQLiteException
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val notesLiveData: MutableLiveData<NoteListResult> = MutableLiveData()

    private var disposable: Disposable? = null

    init {
        updateNotes()
    }

    fun updateNotes() {
        disposable?.dispose()
        disposable = dataManager.notes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { notesLiveData.postValue(NoteListResult.Loading) }
                .subscribeBy(onNext = { notes ->
                    notesLiveData.postValue(NoteListResult.Success(notes))
                }, onError = { throwable ->
                    when (throwable) {
                        is SQLiteException -> notesLiveData.postValue(NoteListResult.NavMain)
                        else -> notesLiveData.postValue(NoteListResult.Error(throwable.message))
                    }
                    Timber.e(throwable)
                })
    }

    override fun onCleared() = disposable?.dispose() ?: Unit
}