package com.softartdev.noteroom.ui.security

import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.di.ConfigPersistent
import com.softartdev.noteroom.ui.base.BasePresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@ConfigPersistent
class SecurityPresenter @Inject
constructor(private val dataManager: DataManager) : BasePresenter<SecurityView>() {

    fun checkEncryption() {
        checkViewAttached()
        addDisposable(dataManager.isEncryption()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ isEncrypted ->
                    mvpView?.showEncryptEnable(isEncrypted)
                }, { throwable ->
                    throwable.printStackTrace()
                    mvpView?.showError(throwable.message)
                }))
    }

    fun changeEncryption(checked: Boolean) {
        checkViewAttached()
        if (checked) {
            mvpView?.showSetPasswordDialog()
        } else {
            addDisposable(dataManager.isEncryption()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ isEncrypted ->
                        if (isEncrypted) {
                            mvpView?.showPasswordDialog()
                        } else {
                            mvpView?.showEncryptEnable(false)
                        }
                    }, { throwable ->
                        throwable.printStackTrace()
                        mvpView?.showError(throwable.message)
                    }))
        }
    }

    fun changePassword() {
        checkViewAttached()
        addDisposable(dataManager.isEncryption()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ isEncrypted ->
                    if (isEncrypted) {
                        mvpView?.showChangePasswordDialog()
                    } else {
                        mvpView?.showSetPasswordDialog()
                    }
                }, { throwable ->
                    throwable.printStackTrace()
                    mvpView?.showError(throwable.message)
                }))
    }

    // only to disable encryption
    fun enterPassCorrect(pass: SecurityView.DialogDirector, completeDismissDialog: () -> Unit) {
        checkViewAttached()
        pass.hideError()
        val password = pass.textString
        if (password.isEmpty()) {
            pass.showEmptyPasswordError()
            mvpView?.showEncryptEnable(true)
        } else {
            addDisposable(dataManager.checkPass(password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap { checked ->
                        if (checked) {
                            completeDismissDialog()
                            mvpView?.showEncryptEnable(false)
                            dataManager.changePass(password, null)
                        } else {
                            Single.fromCallable {
                                mvpView?.showEncryptEnable(true)
                                pass.showIncorrectPasswordError()
                            }
                        }
                    }
                    .subscribe({
                        Timber.d("The password should have been changed")
                    }, { throwable ->
                        throwable.printStackTrace()
                        mvpView?.showError(throwable.message)
                    }))
        }
    }

    // only to enable encryption
    fun setPassCorrect(pass: SecurityView.DialogDirector, repeatPass: SecurityView.DialogDirector, completeDismissDialog: () -> Unit) {
        checkViewAttached()
        pass.hideError()
        repeatPass.hideError()
        val password = pass.textString
        val repeatPassword = repeatPass.textString
        if (password.toString() == repeatPassword.toString()) {
            if (password.isEmpty()) {
                pass.showEmptyPasswordError()
            } else {
                addDisposable(dataManager.changePass(null, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            completeDismissDialog()
                            Timber.d("The password should have been changed")
                            mvpView?.showEncryptEnable(true)
                        }, { throwable ->
                            throwable.printStackTrace()
                            mvpView?.showError(throwable.message)
                        }))
            }
        } else {
            repeatPass.showPasswordsNoMatchError()
        }
    }

    // only when encryption is enabled
    fun changePassCorrect(oldPass: SecurityView.DialogDirector, newPass: SecurityView.DialogDirector, repeatNewPass: SecurityView.DialogDirector, completeDismissDialog: () -> Unit) {
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
            else -> addDisposable(dataManager.checkPass(oldPassword)
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
                        throwable.printStackTrace()
                        mvpView?.showError(throwable.message)
                    }))
        }
    }
}
