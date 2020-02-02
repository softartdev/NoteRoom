package com.softartdev.noteroom.ui.settings.security.enter

import android.text.Editable
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.ui.base.BaseViewModel
import javax.inject.Inject

class EnterViewModel @Inject constructor(
        private val dataManager: DataManager
) : BaseViewModel<EnterResult>() {

    override val loadingResult: EnterResult = EnterResult.Loading

    fun enterCheck(password: Editable) = launch {
        if (password.isNotEmpty()) {
            when (dataManager.checkPass(password)) {
                true -> {
                    dataManager.changePass(password, null)
                    EnterResult.Success
                }
                false -> EnterResult.IncorrectPasswordError
            }
        } else EnterResult.EmptyPasswordError
    }

    override fun errorResult(throwable: Throwable): EnterResult = EnterResult.Error(throwable.message)
}
