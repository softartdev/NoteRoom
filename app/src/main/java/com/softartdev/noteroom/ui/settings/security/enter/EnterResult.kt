package com.softartdev.noteroom.ui.settings.security.enter

import com.softartdev.noteroom.ui.base.ResultFactory

sealed class EnterResult {
    object Loading: EnterResult()
    object Success: EnterResult()
    object EmptyPasswordError: EnterResult()
    object IncorrectPasswordError: EnterResult()
    data class Error(val message: String?): EnterResult()

    class Factory : ResultFactory<EnterResult>() {
        override val loadingResult = Loading
        override fun errorResult(throwable: Throwable) = Error(throwable.message)
    }
}