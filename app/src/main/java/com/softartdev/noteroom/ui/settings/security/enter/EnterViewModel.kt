package com.softartdev.noteroom.ui.settings.security.enter

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

class EnterViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val enterLiveData = MutableLiveData<EnterResult>()

    fun enterCheck(password: Editable) {
        viewModelScope.launch(Dispatchers.IO) {
            onResult(EnterResult.Loading)
            val enterResult: EnterResult = try {
                if (password.isNotEmpty()) {
                    when (dataManager.checkPass(password)) {
                        true -> {
                            dataManager.changePass(password, null)
                            EnterResult.Success
                        }
                        false -> EnterResult.IncorrectPasswordError
                    }
                } else EnterResult.EmptyPasswordError
            } catch (throwable: Throwable) {
                Timber.e(throwable)
                EnterResult.Error(throwable.message)
            }
            onResult(enterResult)
        }
    }

    private suspend fun onResult(enterResult: EnterResult) = withContext(Dispatchers.Main) {
        enterLiveData.value = enterResult
    }
}
