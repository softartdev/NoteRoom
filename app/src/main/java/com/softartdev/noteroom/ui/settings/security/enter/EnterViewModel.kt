package com.softartdev.noteroom.ui.settings.security.enter

import android.text.Editable
import com.softartdev.noteroom.data.CryptUseCase
import com.softartdev.noteroom.ui.base.BaseViewModel
import com.softartdev.noteroom.ui.base.ResultFactory


class EnterViewModel (
        private val dataManager: CryptUseCase
) : BaseViewModel<EnterResult>() {

    fun enterCheck(password: Editable) = launch {
        if (password.isNotEmpty()) {
            when (dataManager.checkPassword(password)) {
                true -> {
                    dataManager.changePassword(password, null)
                    EnterResult.Success
                }
                false -> EnterResult.IncorrectPasswordError
            }
        } else EnterResult.EmptyPasswordError
    }

    override val resultFactory: ResultFactory<EnterResult> = EnterResult.Factory()
}
