package com.softartdev.noteroom.ui.settings.security.change

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softartdev.noteroom.data.DataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
            try {
                changeLiveData.postValue(ChangeResult.Loading)
                val changeResult = when {
                    oldPassword.isEmpty() -> ChangeResult.OldEmptyPasswordError
                    newPassword.isEmpty() -> ChangeResult.NewEmptyPasswordError
                    newPassword.toString() != repeatNewPassword.toString() -> ChangeResult.PasswordsNoMatchError
                    else -> when (val checked = dataManager.checkPass(oldPassword)) {
                        checked -> {
                            dataManager.changePass(oldPassword, newPassword)
                            ChangeResult.Success
                        }
                        else -> ChangeResult.IncorrectPasswordError
                    }
                }
                changeLiveData.postValue(changeResult)
            } catch (throwable: Throwable) {
                Timber.e(throwable)
                changeLiveData.postValue(ChangeResult.Error(throwable.message))
            }
        }
    }

}
