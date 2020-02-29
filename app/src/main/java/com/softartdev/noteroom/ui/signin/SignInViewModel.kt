package com.softartdev.noteroom.ui.signin

import android.text.Editable
import com.softartdev.noteroom.data.CryptUseCase
import com.softartdev.noteroom.ui.base.BaseViewModel
import javax.inject.Inject

class SignInViewModel @Inject constructor(
        private val cryptUseCase: CryptUseCase
) : BaseViewModel<SignInResult>() {

    override val loadingResult: SignInResult = SignInResult.ShowProgress

    fun signIn(pass: Editable) = launch {
        if (pass.isNotEmpty()) {
            when (val checked = cryptUseCase.checkPassword(pass)) {
                checked -> SignInResult.NavMain
                else -> SignInResult.ShowIncorrectPassError
            }
        } else SignInResult.ShowEmptyPassError
    }

    override fun errorResult(throwable: Throwable): SignInResult = SignInResult.ShowError(throwable)
}
