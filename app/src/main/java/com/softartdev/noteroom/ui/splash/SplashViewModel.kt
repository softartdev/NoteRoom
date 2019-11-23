package com.softartdev.noteroom.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crashlytics.android.Crashlytics
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.model.SplashResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val splashLiveData = MutableLiveData<SplashResult>()

    private var disposable: Disposable? = null

    init {
        checkEncryption()
    }

    private fun checkEncryption() {
        disposable?.dispose()
        disposable = dataManager.isEncryption()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ isEncrypted ->
                    if (isEncrypted) {
                        splashLiveData.postValue(SplashResult.NavSignIn)
                    } else {
                        splashLiveData.postValue(SplashResult.NavMain)
                    }
                }, { throwable ->
                    Crashlytics.logException(throwable)
                    throwable.printStackTrace()
                    splashLiveData.postValue(SplashResult.ShowError(throwable.message))
                })
    }

    override fun onCleared() = disposable?.dispose() ?: Unit
}
