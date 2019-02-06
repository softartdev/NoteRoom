package com.softartdev.noteroom.ui.backup

import com.softartdev.noteroom.ui.base.MvpView

interface BackupView : MvpView {
    fun showProgress(show: Boolean)
    fun showBackup()
    fun showError(error: Throwable)
}