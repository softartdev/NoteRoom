package com.softartdev.noteroom.ui.security.enter

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.noteroom.data.DataManager
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class EnterViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val enterLiveData = MutableLiveData<EnterResult>()

    private var disposable: Disposable? = null

    fun enterCheck(password: Editable) {
        disposable?.dispose()
        disposable = Single.just(password)
                .flatMap { passEditable ->
                    if (passEditable.isNotEmpty()) {
                        dataManager.checkPass(password)
                                .flatMap { checked ->
                                    when (checked) {
                                        true -> dataManager.changePass(passEditable, null)
                                                .toSingleDefault(EnterResult.Success)
                                        false -> Single.just(EnterResult.IncorrectPasswordError)
                                    }
                                }
                    } else Single.just(EnterResult.EmptyPasswordError)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = { enterResult ->
                    enterLiveData.value = enterResult
                }, onError = { throwable: Throwable ->
                    Timber.e(throwable)
                    enterLiveData.value = EnterResult.Error(throwable.message)
                })
    }

    override fun onCleared() = disposable?.dispose() ?: Unit
}
