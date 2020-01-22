package com.softartdev.noteroom.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.model.SecurityResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val securityLiveData = MutableLiveData<SecurityResult>()

    fun checkEncryption() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val isEncrypted = dataManager.isEncryption()
                onResult(securityResult = SecurityResult.EncryptEnable(isEncrypted))
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }

    fun changeEncryption(checked: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        try {
            onResult(securityResult = when (checked) {
                true -> SecurityResult.SetPasswordDialog
                false -> when (dataManager.isEncryption()) {
                    true -> SecurityResult.PasswordDialog
                    false -> SecurityResult.EncryptEnable(false)
                }
            })
        } catch (e: Throwable) {
            onError(e)
        }
    }

    fun changePassword() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val isEncrypted = dataManager.isEncryption()
            onResult(securityResult = when {
                isEncrypted -> SecurityResult.ChangePasswordDialog
                else -> SecurityResult.SetPasswordDialog
            })
        } catch (e: Throwable) {
            onError(e)
        }
    }

    private suspend fun onResult(securityResult: SecurityResult) = withContext(Dispatchers.Main) {
        securityLiveData.value = securityResult
    }

    private suspend fun onError(throwable: Throwable) {
        Timber.e(throwable)
        onResult(securityResult = SecurityResult.Error(throwable.message))
    }

}
