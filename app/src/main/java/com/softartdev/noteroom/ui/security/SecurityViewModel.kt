package com.softartdev.noteroom.ui.security

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crashlytics.android.Crashlytics
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.model.SecurityResult
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SecurityViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val securityLiveData = MutableLiveData<SecurityResult>()

    private val compositeDisposable = CompositeDisposable()

    fun checkEncryption() {
        compositeDisposable.add(dataManager.isEncryption()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ isEncrypted ->
                    securityLiveData.postValue(SecurityResult.EncryptEnable(isEncrypted))
                }, { throwable ->
                    Crashlytics.logException(throwable)
                    throwable.printStackTrace()
                    securityLiveData.postValue(SecurityResult.Error(throwable.message))
                }))
    }

    fun changeEncryption(checked: Boolean) {
        if (checked) {
            securityLiveData.postValue(SecurityResult.SetPasswordDialog)
        } else {
            compositeDisposable.add(dataManager.isEncryption()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ isEncrypted ->
                        if (isEncrypted) {
                            securityLiveData.postValue(SecurityResult.PasswordDialog)
                        } else {
                            securityLiveData.postValue(SecurityResult.EncryptEnable(false))
                        }
                    }, { throwable ->
                        Crashlytics.logException(throwable)
                        throwable.printStackTrace()
                        securityLiveData.postValue(SecurityResult.Error(throwable.message))
                    }))
        }
    }

    fun changePassword() {
        compositeDisposable.add(dataManager.isEncryption()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ isEncrypted ->
                    if (isEncrypted) {
                        securityLiveData.postValue(SecurityResult.ChangePasswordDialog)
                    } else {
                        securityLiveData.postValue(SecurityResult.SetPasswordDialog)
                    }
                }, { throwable ->
                    Crashlytics.logException(throwable)
                    throwable.printStackTrace()
                    securityLiveData.postValue(SecurityResult.Error(throwable.message))
                }))
    }

    // only to disable encryption
    fun enterPassCorrect(pass: DialogDirector, completeDismissDialog: () -> Unit) {
        pass.hideError()
        val password = pass.textString
        if (password.isEmpty()) {
            pass.showEmptyPasswordError()
            securityLiveData.postValue(SecurityResult.EncryptEnable(true))
        } else {
            compositeDisposable.add(dataManager.checkPass(password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap { checked ->
                        if (checked) {
                            completeDismissDialog()
                            securityLiveData.postValue(SecurityResult.EncryptEnable(false))
                            dataManager.changePass(password, null)
                        } else {
                            Single.fromCallable {
                                securityLiveData.postValue(SecurityResult.EncryptEnable(true))
                                pass.showIncorrectPasswordError()
                            }
                        }
                    }
                    .subscribe({
                        Timber.d("The password should have been changed")
                    }, { throwable ->
                        Crashlytics.logException(throwable)
                        throwable.printStackTrace()
                        securityLiveData.postValue(SecurityResult.Error(throwable.message))
                    }))
        }
    }

    // only to enable encryption
    fun setPassCorrect(pass: DialogDirector, repeatPass: DialogDirector, completeDismissDialog: () -> Unit) {
        pass.hideError()
        repeatPass.hideError()
        val password = pass.textString
        val repeatPassword = repeatPass.textString
        if (password.toString() == repeatPassword.toString()) {
            if (password.isEmpty()) {
                pass.showEmptyPasswordError()
            } else {
                compositeDisposable.add(dataManager.changePass(null, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            completeDismissDialog()
                            Timber.d("The password should have been changed")
                            securityLiveData.postValue(SecurityResult.EncryptEnable(true))
                        }, { throwable ->
                            Crashlytics.logException(throwable)
                            throwable.printStackTrace()
                            securityLiveData.postValue(SecurityResult.Error(throwable.message))
                        }))
            }
        } else {
            repeatPass.showPasswordsNoMatchError()
        }
    }

    // only when encryption is enabled
    fun changePassCorrect(oldPass: DialogDirector, newPass: DialogDirector, repeatNewPass: DialogDirector, completeDismissDialog: () -> Unit) {
        oldPass.hideError()
        newPass.hideError()
        repeatNewPass.hideError()
        val oldPassword = oldPass.textString
        val newPassword = newPass.textString
        val repeatNewPassword = repeatNewPass.textString
        when {
            oldPassword.isEmpty() -> oldPass.showEmptyPasswordError()
            newPassword.isEmpty() -> newPass.showEmptyPasswordError()
            newPassword.toString() != repeatNewPassword.toString() -> repeatNewPass.showPasswordsNoMatchError()
            else -> compositeDisposable.add(dataManager.checkPass(oldPassword)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap { checked ->
                        if (checked) {
                            completeDismissDialog()
                            dataManager.changePass(oldPassword, newPassword)
                        } else {
                            Single.fromCallable {
                                oldPass.showIncorrectPasswordError()
                            }
                        }
                    }
                    .subscribe({
                        Timber.d("The password should have been changed")
                    }, { throwable ->
                        Crashlytics.logException(throwable)
                        throwable.printStackTrace()
                        securityLiveData.postValue(SecurityResult.Error(throwable.message))
                    }))
        }
    }

    override fun onCleared() = compositeDisposable.clear()
}
