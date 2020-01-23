package com.softartdev.noteroom.ui.settings.security.confirm

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softartdev.noteroom.data.DataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ConfirmViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val confirmLiveData = MutableLiveData<ConfirmResult>()

    fun conformCheck(password: Editable, repeatPassword: Editable) {
        viewModelScope.launch(Dispatchers.IO) {
            onResult(ConfirmResult.Loading)
            val confirmResult = try {
                when {
                    password.toString() != repeatPassword.toString() -> ConfirmResult.PasswordsNoMatchError
                    password.isEmpty() -> ConfirmResult.EmptyPasswordError
                    else -> {
                        dataManager.changePass(null, password)
                        ConfirmResult.Success
                    }
                }
            } catch (throwable: Throwable) {
                Timber.e(throwable)
                ConfirmResult.Error(throwable.message)
            }
            onResult(confirmResult)
        }
    }

    private suspend fun onResult(confirmResult: ConfirmResult) = withContext(Dispatchers.Main) {
        confirmLiveData.value = confirmResult
    }
}
