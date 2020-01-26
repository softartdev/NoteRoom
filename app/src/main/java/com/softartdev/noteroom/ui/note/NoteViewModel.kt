package com.softartdev.noteroom.ui.note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.model.NoteResult
import com.softartdev.noteroom.util.createTitle
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

    private var noteId: Long = 0
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

    fun editTitle() = launch {
        subscribeToEditTitle()
        Single.just(NoteResult.NavEditTitle(noteId))
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
                .doOnSubscribe { onResult(noteResult = NoteResult.Loading) }
                .subscribeBy(onSuccess = this::onResult, onError = this::onError))
    }

    private fun subscribeToEditTitle() = compositeDisposable.add(
            dataManager.titleSubject
                    .map { NoteResult.TitleUpdated(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(onNext = this::onResult, onError = this::onError))

    private fun onResult(noteResult: NoteResult) {
        noteLiveData.value = noteResult
    }

    private fun onError(throwable: Throwable) {
        Timber.e(throwable)
        onResult(noteResult = NoteResult.Error(throwable.message))
    }

    override fun onCleared() = compositeDisposable.clear()
}