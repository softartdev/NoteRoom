package com.softartdev.noteroom.ui.settings.security.confirm

import android.text.Editable
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.ui.base.BaseViewModel
import javax.inject.Inject

class ConfirmViewModel @Inject constructor(
        private val dataManager: DataManager
) : BaseViewModel<ConfirmResult>() {

    override val loadingResult: ConfirmResult = ConfirmResult.Loading

    fun conformCheck(password: Editable, repeatPassword: Editable) = launch {
        when {
            password.toString() != repeatPassword.toString() -> ConfirmResult.PasswordsNoMatchError
            password.isEmpty() -> ConfirmResult.EmptyPasswordError
            else -> {
                dataManager.changePass(null, password)
                ConfirmResult.Success
            }
        }
    }

    override fun errorResult(throwable: Throwable): ConfirmResult = ConfirmResult.Error(throwable.message)
}
