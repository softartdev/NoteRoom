package com.softartdev.noteroom.ui.settings

import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.ui.base.BaseViewModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
        private val dataManager: DataManager
) : BaseViewModel<SecurityResult>() {

    fun checkEncryption() = launch {
        val isEncrypted = dataManager.isEncryption()
        SecurityResult.EncryptEnable(isEncrypted)
    }

    fun changeEncryption(checked: Boolean) = launch {
        when (checked) {
            true -> SecurityResult.SetPasswordDialog
            false -> when (dataManager.isEncryption()) {
                true -> SecurityResult.PasswordDialog
                false -> SecurityResult.EncryptEnable(false)
            }
        }
    }

    fun changePassword() = launch {
            when(dataManager.isEncryption()) {
                true -> SecurityResult.ChangePasswordDialog
                false -> SecurityResult.SetPasswordDialog
            }
        }

    override fun errorResult(throwable: Throwable): SecurityResult = SecurityResult.Error(throwable.message)
}
