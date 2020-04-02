package com.softartdev.noteroom.ui.settings.security.confirm

import android.text.Editable
import com.softartdev.noteroom.data.CryptUseCase
import com.softartdev.noteroom.ui.base.BaseViewModel
import com.softartdev.noteroom.ui.base.ResultFactory


class ConfirmViewModel (
        private val cryptUseCase: CryptUseCase
) : BaseViewModel<ConfirmResult>() {

    fun conformCheck(password: Editable, repeatPassword: Editable) = launch {
        when {
            password.toString() != repeatPassword.toString() -> ConfirmResult.PasswordsNoMatchError
            password.isEmpty() -> ConfirmResult.EmptyPasswordError
            else -> {
                cryptUseCase.changePassword(null, password)
                ConfirmResult.Success
            }
        }
    }

    override val resultFactory: ResultFactory<ConfirmResult> = ConfirmResult.Factory()
}
