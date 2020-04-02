package com.softartdev.noteroom.ui.splash

import com.softartdev.noteroom.data.CryptUseCase
import com.softartdev.noteroom.ui.base.BaseViewModel
import com.softartdev.noteroom.ui.base.ResultFactory

class SplashViewModel(
        private val cryptUseCase: CryptUseCase
) : BaseViewModel<SplashResult>() {

    init {
        checkEncryption()
    }

    private fun checkEncryption() = launch {
        when (cryptUseCase.dbIsEncrypted()) {
            true -> SplashResult.NavSignIn
            false -> SplashResult.NavMain
        }
    }

    override val resultFactory: ResultFactory<SplashResult> = SplashResult.Factory()
}
