package com.softartdev.noteroom.ui.settings.security.change

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

class ChangeViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val changeLiveData = MutableLiveData<ChangeResult>()

    fun checkChange(
            oldPassword: Editable,
            newPassword: Editable,
            repeatNewPassword: Editable
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            onResult(ChangeResult.Loading)
            val changeResult = try {
                when {
                    oldPassword.isEmpty() -> ChangeResult.OldEmptyPasswordError
                    newPassword.isEmpty() -> ChangeResult.NewEmptyPasswordError
                    newPassword.toString() != repeatNewPassword.toString() -> ChangeResult.PasswordsNoMatchError
                    else -> when (dataManager.checkPass(oldPassword)) {
                        true -> {
                            dataManager.changePass(oldPassword, newPassword)
                            ChangeResult.Success
                        }
                        false -> ChangeResult.IncorrectPasswordError
                    }
                }
            } catch (throwable: Throwable) {
                Timber.e(throwable)
                ChangeResult.Error(throwable.message)
            }
            onResult(changeResult)
        }
    }

    private suspend fun onResult(changeResult: ChangeResult) = withContext(Dispatchers.Main) {
        changeLiveData.value = changeResult
    }
}
