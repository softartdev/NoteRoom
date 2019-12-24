package com.softartdev.noteroom.ui.security.enter

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.noteroom.data.DataManager
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class EnterViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val enterLiveData = MutableLiveData<EnterResult>()

    private val compositeDisposable = CompositeDisposable()

    fun enterCheck(password: Editable?) {
        if (password.isNullOrEmpty()) {
            enterLiveData.value = EnterResult.EmptyPasswordError
        } else compositeDisposable.add(dataManager.checkPass(password)
                .flatMapCompletable { checked ->
                    if (checked) {
                        dataManager.changePass(password, null).doOnSuccess {
                            enterLiveData.postValue(EnterResult.Success)
                        }.ignoreElement()
                    } else Completable.fromCallable {
                        enterLiveData.postValue(EnterResult.IncorrectPasswordError)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onComplete = {
                    Timber.d("The password should have been changed")
                }, onError = { throwable: Throwable ->
                    Timber.e(throwable)
                    enterLiveData.value = EnterResult.Error(throwable.message)
                }))
    }

    override fun onCleared() = compositeDisposable.clear()

}