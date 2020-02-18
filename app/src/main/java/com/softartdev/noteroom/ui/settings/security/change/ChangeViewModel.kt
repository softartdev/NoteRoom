package com.softartdev.noteroom.ui.settings.security.change

import android.text.Editable
import com.softartdev.noteroom.old.DataManager
import com.softartdev.noteroom.ui.base.BaseViewModel
import javax.inject.Inject

class ChangeViewModel @Inject constructor(
        private val dataManager: DataManager
) : BaseViewModel<ChangeResult>() {

    override val loadingResult: ChangeResult = ChangeResult.Loading

    fun checkChange(
            oldPassword: Editable,
            newPassword: Editable,
            repeatNewPassword: Editable
    ) = launch {
        when {
            oldPassword.isEmpty() -> ChangeResult.OldEmptyPasswordError
            newPassword.isEmpty() -> ChangeResult.NewEmptyPasswordError
            newPassword.toString() != repeatNewPassword.toString() -> ChangeResult.PasswordsNoMatchError
            else -> when (dataManager.checkPass(oldPassword)) {
                true -> {
                    dataManager.changePass(oldPassword, newPassword)
                    ChangeResult.Success
                }
                false -> ChangeResult.IncorrectPasswordError
            }
        }
    }

    override fun errorResult(throwable: Throwable): ChangeResult = ChangeResult.Error(throwable.message)
}
