package com.softartdev.noteroom.ui.security.confirm

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

class ConfirmViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val confirmLiveData = MutableLiveData<ConfirmResult>()

    private var disposable: Disposable? = null

    fun conformCheck(password: Editable, repeatPassword: Editable) {
        disposable?.dispose()
        disposable = Single.just(password to repeatPassword)
                .flatMap { pair ->
                    val (passEditable, repeatPassEditable) = pair
                    when {
                        passEditable.toString() != repeatPassEditable.toString() ->
                            Single.just(ConfirmResult.PasswordsNoMatchError)
                        passEditable.isEmpty() ->
                            Single.just(ConfirmResult.EmptyPasswordError)
                        else -> dataManager.changePass(null, password)
                                .map { ConfirmResult.Success }
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = { confirmResult ->
                    confirmLiveData.value = confirmResult
                }, onError = { throwable ->
                    Timber.e(throwable)
                    confirmLiveData.value = ConfirmResult.Error(throwable.message)
                })
    }

    override fun onCleared() = disposable?.dispose() ?: Unit
}
