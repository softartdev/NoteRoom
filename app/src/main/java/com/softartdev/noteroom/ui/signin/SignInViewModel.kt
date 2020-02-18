package com.softartdev.noteroom.ui.signin

import android.text.Editable
import com.softartdev.noteroom.old.DataManager
import com.softartdev.noteroom.ui.base.BaseViewModel
import javax.inject.Inject

class SignInViewModel @Inject constructor(
        private val dataManager: DataManager
) : BaseViewModel<SignInResult>() {

    override val loadingResult: SignInResult = SignInResult.ShowProgress

    fun signIn(pass: Editable) = launch {
        if (pass.isNotEmpty()) {
            when (val checked = dataManager.checkPass(pass)) {
                checked -> SignInResult.NavMain
                else -> SignInResult.ShowIncorrectPassError
            }
        } else SignInResult.ShowEmptyPassError
    }

    override fun errorResult(throwable: Throwable): SignInResult = SignInResult.ShowError(throwable)
}
