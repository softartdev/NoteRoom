package com.softartdev.noteroom.ui.note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.model.NoteResult
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class NoteViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val noteLiveData: MutableLiveData<NoteResult> = MutableLiveData()

    internal var noteId: Long = 0
        get() = when (field) {
            0L -> throw IllegalStateException()
            else -> field
        }

    private val compositeDisposable = CompositeDisposable()

    fun createNote() = launch {
        dataManager.createNote()
                .map {
                    noteId = it
                    Timber.d("Created note with id=$noteId")
                    NoteResult.Created(it)
                }
    }

    fun loadNote(id: Long) = launch {
        dataManager.loadNote(id)
                .flatMapSingle {
                    noteId = it.id
                    Timber.d("Loaded note with id=$noteId")
                    Single.just(NoteResult.Loaded(it))
                }
    }

    fun saveNote(title: String?, text: String) = launch {
        Single.just(title to text)
                .flatMap { pair ->
                    var (noteTitle, noteText) = pair
                    if (noteTitle.isNullOrEmpty() && noteText.isEmpty()) {
                        Single.just(NoteResult.Empty)
                    } else {
                        noteTitle = noteTitle ?: createTitle(text)
                        dataManager.saveNote(noteId, noteTitle, noteText)
                                .map {
                                    Timber.d("Saved note with id=$noteId")
                                    NoteResult.Saved(noteTitle)
                                }
                    }
                }
    }

    fun deleteNote() = launch { deleteNoteSingle() }

    fun checkSaveChange(title: String?, text: String) = launch {
        Single.just(title to text)
                .flatMap { pair ->
                    var (noteTitle, noteText) = pair
                    noteTitle = noteTitle ?: createTitle(text)
                    Single.zip(
                            dataManager.checkChanges(noteId, noteTitle, noteText),
                            dataManager.emptyNote(noteId),
                            BiFunction<Boolean, Boolean, Single<NoteResult>> { changed, empty ->
                                when {
                                    changed -> Single.just(NoteResult.CheckSaveChange)
                                    empty -> deleteNoteSingle()
                                    else -> Single.just(NoteResult.NavBack)
                                }
                            }
                    )
                }.flatMap { it }
    }

    fun saveNoteAndNavBack(title: String?, text: String) = launch {
        Single.just(title to text)
                .flatMap { pair ->
                    var (noteTitle, noteText) = pair
                    noteTitle = noteTitle ?: createTitle(text)
                    dataManager.saveNote(noteId, noteTitle, noteText)
                            .map {
                                Timber.d("Saved and nav back")
                                NoteResult.NavBack
                            }
                }
    }

    fun doNotSaveAndNavBack() = launch {
        dataManager.emptyNote(noteId)
                .flatMap { noteIsEmpty ->
                    if (noteIsEmpty) {
                        deleteNoteSingle()
                    } else {
                        Timber.d("Don't save and nav back")
                        Single.just(NoteResult.NavBack)
                    }
                }
    }

    private fun deleteNoteSingle(): Single<NoteResult> = dataManager.deleteNote(noteId).map {
        Timber.d("Deleted note with id=$noteId")
        NoteResult.Deleted
    }

    private fun launch(job: () -> Single<NoteResult>) {
        compositeDisposable.add(job()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { noteLiveData.value = NoteResult.Loading }
                .subscribeBy(onSuccess = { editTitleResult ->
                    noteLiveData.value = editTitleResult
                }, onError = { throwable: Throwable ->
                    Timber.e(throwable)
                    noteLiveData.value = NoteResult.Error(throwable.message)
                }))
    }

    private fun createTitle(text: String): String {
        // Get the note's length
        val length = text.length

        // Sets the title by getting a substring of the text that is 31 characters long
        // or the number of characters in the note plus one, whichever is smaller.
        var title = text.substring(0, 30.coerceAtMost(length))

        // If the resulting length is more than 30 characters, chops off any
        // trailing spaces
        if (length > 30) {
            val lastSpace: Int = title.lastIndexOf(' ')
            if (lastSpace > 0) {
                title = title.substring(0, lastSpace)
            }
        }
        return title
    }

    override fun onCleared() = compositeDisposable.clear()
}