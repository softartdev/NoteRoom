package com.softartdev.noteroom.ui.splash

import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.ui.base.BaseViewModel
import javax.inject.Inject

class SplashViewModel @Inject constructor(
        private val dataManager: DataManager
) : BaseViewModel<SplashResult>() {

    init {
        checkEncryption()
    }

    private fun checkEncryption() = launch {
        when (dataManager.isEncryption()) {
            true -> SplashResult.NavSignIn
            false -> SplashResult.NavMain
        }
    }

    override fun errorResult(throwable: Throwable): SplashResult = SplashResult.ShowError(throwable.message)
}
