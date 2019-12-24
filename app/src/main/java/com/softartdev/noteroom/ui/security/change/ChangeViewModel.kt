package com.softartdev.noteroom.ui.security.change

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.noteroom.data.DataManager
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ChangeViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val changeLiveData = MutableLiveData<ChangeResult>()

    private val compositeDisposable = CompositeDisposable()

    fun checkChange(oldPassword: Editable?, newPassword: Editable?, repeatNewPassword: Editable?) = when {
        oldPassword.isNullOrEmpty() -> {
            changeLiveData.value = ChangeResult.OldEmptyPasswordError
        }
        newPassword.isNullOrEmpty() -> {
            changeLiveData.value = ChangeResult.NewEmptyPasswordError
        }
        newPassword.toString() != repeatNewPassword.toString() -> {
            changeLiveData.value = ChangeResult.PasswordsNoMatchError
        }
        else -> {
            compositeDisposable.add(dataManager.checkPass(oldPassword)
                    .flatMapCompletable { checked ->
                        if (checked) {
                            dataManager.changePass(oldPassword, newPassword).doOnSuccess {
                                changeLiveData.postValue(ChangeResult.Success)
                            }.ignoreElement()
                        } else Completable.fromCallable {
                            changeLiveData.postValue(ChangeResult.IncorrectPasswordError)
                        }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Timber.d("The password should have been changed")
                    }, { throwable ->
                        Timber.e(throwable)
                        changeLiveData.value = ChangeResult.Error(throwable.message)
                    })); Unit
        }
    }

    override fun onCleared() = compositeDisposable.clear()
}
