package com.softartdev.noteroom.ui.title

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.noteroom.data.DataManager
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class EditTitleViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val editTitleLiveData = MutableLiveData<EditTitleResult>()

    private val compositeDisposable = CompositeDisposable()

    fun loadTitle(noteId: Long) = launch {
        dataManager.loadNote(noteId)
                .toSingle()
                .map { EditTitleResult.Loaded(it.title) }
    }

    fun editTitle(newTitle: String) = launch {
        Single.just(newTitle)
                .flatMap { title ->
                    if (title.isNotEmpty()) {
                        //TODO
                        Single.just(EditTitleResult.Success)
                    } else Single.just(EditTitleResult.EmptyTitleError)
                }
    }

    private fun launch(job: () -> Single<EditTitleResult>) {
        compositeDisposable.add(job()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { editTitleLiveData.value = EditTitleResult.Loading }
                .subscribeBy(onSuccess = { editTitleResult ->
                    editTitleLiveData.value = editTitleResult
                }, onError = { throwable: Throwable ->
                    Timber.e(throwable)
                    editTitleLiveData.value = EditTitleResult.Error(throwable.message)
                }))
    }

    override fun onCleared() = compositeDisposable.clear()
}
