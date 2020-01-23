package com.softartdev.noteroom.ui.signin

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.model.SignInResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SignInViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val signInLiveData = MutableLiveData<SignInResult>()

    fun signIn(pass: Editable) = viewModelScope.launch(Dispatchers.IO) {
        onResult(SignInResult.ShowProgress)
        val signInResult = try {
            if (pass.isNotEmpty()) {
                when (val checked = dataManager.checkPass(pass)) {
                    checked -> SignInResult.NavMain
                    else -> SignInResult.ShowIncorrectPassError
                }
            } else SignInResult.ShowEmptyPassError
        } catch (throwable: Throwable) {
            Timber.e(throwable)
            SignInResult.ShowError(throwable)
        }
        onResult(signInResult)
    }

    private suspend fun onResult(signInResult: SignInResult) = withContext(Dispatchers.Main) {
        signInLiveData.value = signInResult
    }
}
