package com.softartdev.noteroom.ui.signin

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.model.SignInResult
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SignInViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val signInLiveData = MutableLiveData<SignInResult>()

    private var disposable: Disposable? = null

    fun signIn(pass: Editable) {
        disposable?.dispose()
        disposable = Single.just(pass)
                .flatMap { passEditable ->
                    if (passEditable.isNotEmpty()) {
                        dataManager.checkPass(passEditable)
                                .map { checked ->
                                    when (checked) {
                                        true -> SignInResult.NavMain
                                        false -> SignInResult.ShowIncorrectPassError
                                    }
                                }
                    } else Single.just(SignInResult.ShowEmptyPassError)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { signInLiveData.value = SignInResult.ShowProgress }
                .subscribeBy(onSuccess = { signInResult ->
                    signInLiveData.value = signInResult
                }, onError = { throwable ->
                    Timber.e(throwable)
                    signInLiveData.value = SignInResult.ShowError(throwable)
                })
    }

    override fun onCleared() = disposable?.dispose() ?: Unit
}
