package com.softartdev.noteroom.ui.base

abstract class ResultFactory<T> {
    open val loadingResult: T? = null
    abstract fun errorResult(throwable: Throwable): T
}