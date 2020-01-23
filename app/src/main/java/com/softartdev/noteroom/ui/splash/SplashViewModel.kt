package com.softartdev.noteroom.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.model.SplashResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SplashViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val splashLiveData = MutableLiveData<SplashResult>()

    init {
        checkEncryption()
    }

    private fun checkEncryption() = viewModelScope.launch(Dispatchers.IO) {
        val splashResult: SplashResult = try {
            when (dataManager.isEncryption()) {
                true -> SplashResult.NavSignIn
                false -> SplashResult.NavMain
            }
        } catch (throwable: Throwable) {
            Timber.e(throwable)
            SplashResult.ShowError(throwable.message)
        }
        withContext(Dispatchers.Main) {
            splashLiveData.value = splashResult
        }
    }

}
