package com.softartdev.noteroom.ui.title

sealed class EditTitleResult {
    object Loading: EditTitleResult()
    data class Loaded(val title: String): EditTitleResult()
    object Success: EditTitleResult()
    object EmptyTitleError: EditTitleResult()
    data class Error(val message: String?): EditTitleResult()
}