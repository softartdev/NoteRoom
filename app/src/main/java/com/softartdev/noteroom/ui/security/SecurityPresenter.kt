package com.softartdev.noteroom.ui.security

import android.text.TextUtils
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.di.ConfigPersistent
import com.softartdev.noteroom.ui.base.BasePresenter
import timber.log.Timber
import javax.inject.Inject

@ConfigPersistent
class SecurityPresenter @Inject
constructor(private val dataManager: DataManager) : BasePresenter<SecurityView>() {

    val isEncryption: Boolean
        get() = dataManager.isEncryption()

    private fun checkPass(pass: String?): Boolean {
        return dataManager.checkPass(pass)
    }

    private fun changePassword(odlPass: String?, newPass: String?) {
        addDisposable(dataManager.changePass(odlPass, newPass)
                .subscribe(
                        { Timber.d("Password changed") }
                        , { throwable ->
                            throwable.printStackTrace()
                            mvpView?.showError(throwable.message)
                }))
    }

    fun changeEncryption(checked: Boolean) {
        checkViewAttached()
        if (checked) {
            mvpView?.showSetPasswordDialog()
        } else {
            if (isEncryption) {
                mvpView?.showPasswordDialog()
            } else {
                mvpView?.showEncryptEnable(false)
            }
        }
    }

    fun changePassword() {
        checkViewAttached()
        if (isEncryption) {
            mvpView?.showChangePasswordDialog()
        } else {
            mvpView?.showSetPasswordDialog()
        }
    }

    // only to disable encryption
    fun enterPassCorrect(pass: SecurityView.DialogDirector): Boolean {
        checkViewAttached()
        pass.hideError()

        val password = pass.textString
        when {
            TextUtils.isEmpty(password) -> {
                pass.showEmptyPasswordError()
                mvpView?.showEncryptEnable(true)
            }
            checkPass(password) -> {
                changePassword(password, null)
                mvpView?.showEncryptEnable(false)
                return true
            }
            else -> {
                pass.showIncorrectPasswordError()
                mvpView?.showEncryptEnable(true)
            }
        }

        return false
    }

    // only to enable encryption
    fun setPassCorrect(pass: SecurityView.DialogDirector, repeatPass: SecurityView.DialogDirector): Boolean {
        checkViewAttached()
        pass.hideError()
        repeatPass.hideError()

        val password = pass.textString
        val repeatPassword = repeatPass.textString
        if (password == repeatPassword) {
            if (TextUtils.isEmpty(password)) {
                pass.showEmptyPasswordError()
            } else {
                changePassword(null, password)
                mvpView?.showEncryptEnable(true)
                return true
            }
        } else {
            repeatPass.showPasswordsNoMatchError()
        }

        return false
    }

    // only when encryption is enabled
    fun changePassCorrect(oldPass: SecurityView.DialogDirector, newPass: SecurityView.DialogDirector, repeatNewPass: SecurityView.DialogDirector): Boolean {
        oldPass.hideError()
        newPass.hideError()
        repeatNewPass.hideError()

        val oldPassword = oldPass.textString
        val newPassword = newPass.textString
        val repeatNewPassword = repeatNewPass.textString
        when {
            TextUtils.isEmpty(oldPassword) -> oldPass.showEmptyPasswordError()
            checkPass(oldPassword) -> when {
                TextUtils.isEmpty(newPassword) -> newPass.showEmptyPasswordError()
                newPassword == repeatNewPassword -> {
                    changePassword(oldPassword, newPassword)
                    return true
                }
                else -> repeatNewPass.showPasswordsNoMatchError()
            }
            else -> oldPass.showIncorrectPasswordError()
        }

        return false
    }
}
