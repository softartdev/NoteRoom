package com.softartdev.noteroom.ui.settings.security.enter

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softartdev.noteroom.data.DataManager
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class EnterViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    private val _enterLiveData = MutableLiveData<EnterResult>()
    val enterLiveData: LiveData<EnterResult> = _enterLiveData

    fun enterCheck(password: Editable) {
        viewModelScope.launch {
            _enterLiveData.value = EnterResult.Loading
            _enterLiveData.value = try {
                if (password.isNotEmpty()) {
                    when (val checked = dataManager.checkPass(password)) {
                        checked -> {
                            dataManager.changePass(password, null)
                            EnterResult.Success
                        }
                        else -> EnterResult.IncorrectPasswordError
                    }
                } else EnterResult.EmptyPasswordError
            } catch (throwable: Throwable) {
                Timber.e(throwable)
                EnterResult.Error(throwable.message)
            }
        }
    }
}
