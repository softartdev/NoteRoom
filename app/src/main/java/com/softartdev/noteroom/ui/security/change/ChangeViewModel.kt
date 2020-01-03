package com.softartdev.noteroom.ui.security.change

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

class ChangeViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val changeLiveData = MutableLiveData<ChangeResult>()

    private var disposable: Disposable? = null

    fun checkChange(oldPassword: Editable, newPassword: Editable, repeatNewPassword: Editable) {
        disposable?.dispose()
        disposable = Single.just(Triple(oldPassword, newPassword, repeatNewPassword))
                .flatMap { triple ->
                    val (oldEditable, newEditable, repeatEditable) = triple
                    when {
                        oldEditable.isEmpty() ->
                            Single.just(ChangeResult.OldEmptyPasswordError)
                        newEditable.isEmpty() ->
                            Single.just(ChangeResult.NewEmptyPasswordError)
                        newEditable.toString() != repeatEditable.toString() ->
                            Single.just(ChangeResult.PasswordsNoMatchError)
                        else -> dataManager.checkPass(oldEditable)
                                .flatMap { checked ->
                                    when (checked) {
                                        true -> dataManager.changePass(oldEditable, newEditable)
                                                .toSingleDefault(ChangeResult.Success)
                                        false -> Single.just(ChangeResult.IncorrectPasswordError)
                                    }
                                }
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { changeLiveData.value = ChangeResult.Loading }
                .subscribeBy(onSuccess = { changeResult ->
                    changeLiveData.value = changeResult
                }, onError = { throwable ->
                    Timber.e(throwable)
                    changeLiveData.value = ChangeResult.Error(throwable.message)
                })
    }

    override fun onCleared() = disposable?.dispose() ?: Unit
}
