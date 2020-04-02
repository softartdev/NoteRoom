package com.softartdev.noteroom.ui.signin

import android.text.Editable
import com.softartdev.noteroom.data.CryptUseCase
import com.softartdev.noteroom.ui.base.BaseViewModel
import com.softartdev.noteroom.ui.base.ResultFactory


class SignInViewModel (
        private val cryptUseCase: CryptUseCase
) : BaseViewModel<SignInResult>() {

    fun signIn(pass: Editable) = launch {
        if (pass.isNotEmpty()) {
            when (val checked = cryptUseCase.checkPassword(pass)) {
                checked -> SignInResult.NavMain
                else -> SignInResult.ShowIncorrectPassError
            }
        } else SignInResult.ShowEmptyPassError
    }

    override val resultFactory: ResultFactory<SignInResult> = SignInResult.Factory()
}
