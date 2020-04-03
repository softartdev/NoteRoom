package com.softartdev.noteroom.ui.signin

import android.text.Editable
import com.softartdev.noteroom.data.CryptUseCase
import com.softartdev.noteroom.ui.base.BaseViewModel


class SignInViewModel (
        private val cryptUseCase: CryptUseCase
) : BaseViewModel<SignInResult>() {

    override val loadingResult: SignInResult = SignInResult.ShowProgress

    fun signIn(pass: Editable) = launch {
        if (pass.isNotEmpty()) {
            when (cryptUseCase.checkPassword(pass)) {
                true -> SignInResult.NavMain
                false -> SignInResult.ShowIncorrectPassError
            }
        } else SignInResult.ShowEmptyPassError
    }

    override fun errorResult(throwable: Throwable): SignInResult = SignInResult.ShowError(throwable)
}
