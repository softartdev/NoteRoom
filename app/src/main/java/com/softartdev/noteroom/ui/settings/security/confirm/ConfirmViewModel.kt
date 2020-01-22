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
        viewModelScope.launch {
            try {
                confirmLiveData.postValue(ConfirmResult.Loading)
                val confirmResult = when {
                    password.toString() != repeatPassword.toString() -> ConfirmResult.PasswordsNoMatchError
                    password.isEmpty() -> ConfirmResult.EmptyPasswordError
                    else -> withContext(Dispatchers.IO) {
                        dataManager.changePass(null, password)
                        ConfirmResult.Success
                    }
                }
                confirmLiveData.postValue(confirmResult)
            } catch (throwable: Throwable) {
                withContext(Dispatchers.Main) {
                    confirmLiveData.postValue(ConfirmResult.Error(throwable.message))
                }
                Timber.e(throwable)
            }
        }
    }
}
