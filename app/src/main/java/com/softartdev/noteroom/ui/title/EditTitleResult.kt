package com.softartdev.noteroom.ui.title

import com.softartdev.noteroom.ui.base.ResultFactory

sealed class EditTitleResult {
    object Loading: EditTitleResult()
    data class Loaded(val title: String): EditTitleResult()
    object Success: EditTitleResult()
    object EmptyTitleError: EditTitleResult()
    data class Error(val message: String?): EditTitleResult()

    class Factory : ResultFactory<EditTitleResult>() {
        override val loadingResult = Loading
        override fun errorResult(throwable: Throwable) = Error(throwable.message)
    }
}