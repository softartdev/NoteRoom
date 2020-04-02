package com.softartdev.noteroom.ui.settings.security.change

import android.text.Editable
import com.softartdev.noteroom.data.CryptUseCase
import com.softartdev.noteroom.ui.base.BaseViewModel
import com.softartdev.noteroom.ui.base.ResultFactory


class ChangeViewModel (
        private val cryptUseCase: CryptUseCase
) : BaseViewModel<ChangeResult>() {

    fun checkChange(
            oldPassword: Editable,
            newPassword: Editable,
            repeatNewPassword: Editable
    ) = launch {
        when {
            oldPassword.isEmpty() -> ChangeResult.OldEmptyPasswordError
            newPassword.isEmpty() -> ChangeResult.NewEmptyPasswordError
            newPassword.toString() != repeatNewPassword.toString() -> ChangeResult.PasswordsNoMatchError
            else -> when (cryptUseCase.checkPassword(oldPassword)) {
                true -> {
                    cryptUseCase.changePassword(oldPassword, newPassword)
                    ChangeResult.Success
                }
                false -> ChangeResult.IncorrectPasswordError
            }
        }
    }

    override val resultFactory: ResultFactory<ChangeResult> = ChangeResult.Factory()
}
