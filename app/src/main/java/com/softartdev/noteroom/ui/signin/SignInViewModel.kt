package com.softartdev.noteroom.ui.signin

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.model.SignInResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
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
        signInLiveData.postValue(SignInResult.HideError)
        if (pass.isEmpty()) {
            signInLiveData.postValue(SignInResult.ShowEmptyPassError)
        } else {
            signInLiveData.postValue(SignInResult.ShowProgress(true))
            disposable = dataManager.checkPass(pass)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ checked ->
                        signInLiveData.postValue(SignInResult.ShowProgress(false))
                        if (checked) {
                            signInLiveData.postValue(SignInResult.NavMain)
                        } else {
                            signInLiveData.postValue(SignInResult.ShowIncorrectPassError)
                        }
                    }, { throwable ->
                        Timber.e(throwable)
                        signInLiveData.postValue(SignInResult.ShowProgress(false))
                        signInLiveData.postValue(SignInResult.ShowError(throwable))
                    })
        }
    }

    override fun onCleared() = disposable?.dispose() ?: Unit
}
