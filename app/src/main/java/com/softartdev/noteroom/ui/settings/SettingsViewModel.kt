package com.softartdev.noteroom.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.model.SecurityResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val securityLiveData = MutableLiveData<SecurityResult>()

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

    private fun launch(
            block: suspend CoroutineScope.() -> SecurityResult
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val securityResult = try {
                block()
            } catch (throwable: Throwable) {
                Timber.e(throwable)
                SecurityResult.Error(throwable.message)
            }
            withContext(Dispatchers.Main) {
                securityLiveData.value = securityResult
            }
        }
    }

}
