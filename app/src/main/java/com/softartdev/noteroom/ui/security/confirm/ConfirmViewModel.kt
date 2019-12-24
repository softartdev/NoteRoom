package com.softartdev.noteroom.ui.security.confirm

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.noteroom.data.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ConfirmViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val confirmLiveData = MutableLiveData<ConfirmResult>()

    private val compositeDisposable = CompositeDisposable()

    fun conformCheck(password: Editable?, repeatPassword: Editable?) = when {
        password.toString() != repeatPassword.toString() -> {
            confirmLiveData.value = ConfirmResult.PasswordsNoMatchError
        }
        password.isNullOrEmpty() -> {
            confirmLiveData.value = ConfirmResult.EmptyPasswordError
        }
        else -> {
            compositeDisposable.add(dataManager.changePass(null, password)
                    .ignoreElement()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(onComplete = {
                        Timber.d("The password should have been changed")
                        confirmLiveData.value = ConfirmResult.Success
                    }, onError =  { throwable ->
                        Timber.e(throwable)
                        confirmLiveData.value = ConfirmResult.Error(throwable.message)
                    })); Unit
        }
    }

    override fun onCleared() = compositeDisposable.clear()
}
