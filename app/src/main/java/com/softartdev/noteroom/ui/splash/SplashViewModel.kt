package com.softartdev.noteroom.ui.splash

import com.softartdev.noteroom.data.CryptUseCase
import com.softartdev.noteroom.ui.base.BaseViewModel

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

    override fun errorResult(throwable: Throwable): SplashResult = SplashResult.ShowError(throwable.message)
}
