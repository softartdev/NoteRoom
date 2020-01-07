package com.softartdev.noteroom.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.model.SecurityResult
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val securityLiveData = MutableLiveData<SecurityResult>()

    private val compositeDisposable = CompositeDisposable()

    fun checkEncryption() {
        compositeDisposable.add(dataManager.isEncryption()
                .map { isEncrypted ->
                    SecurityResult.EncryptEnable(isEncrypted)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = this::onResult, onError =  this::onError))
    }

    fun changeEncryption(checked: Boolean) {
        compositeDisposable.add(Single.just(checked)
                .flatMap { isChecked ->
                    when {
                        isChecked -> Single.just(SecurityResult.SetPasswordDialog)
                        else -> dataManager.isEncryption()
                                .map { isEncrypted ->
                                    when {
                                        isEncrypted -> SecurityResult.PasswordDialog
                                        else -> SecurityResult.EncryptEnable(false)
                                    }
                                }
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = this::onResult, onError =  this::onError))
    }

    fun changePassword() {
        compositeDisposable.add(dataManager.isEncryption()
                .map { isEncrypted ->
                    when {
                        isEncrypted -> SecurityResult.ChangePasswordDialog
                        else -> SecurityResult.SetPasswordDialog
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = this::onResult, onError =  this::onError))
    }

    private fun onResult(securityResult: SecurityResult) {
        securityLiveData.value = securityResult
    }

    private fun onError(throwable: Throwable) {
        Timber.e(throwable)
        onResult(securityResult = SecurityResult.Error(throwable.message))
    }

    override fun onCleared() = compositeDisposable.clear()
}
