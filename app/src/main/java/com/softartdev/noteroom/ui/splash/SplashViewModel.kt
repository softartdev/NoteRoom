package com.softartdev.noteroom.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.model.SplashResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
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
                .map { isEncrypted ->
                    when {
                        isEncrypted -> SplashResult.NavSignIn
                        else -> SplashResult.NavMain
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = {
                    splashLiveData.value = it
                }, onError = { throwable ->
                    Timber.e(throwable)
                    splashLiveData.value = SplashResult.ShowError(throwable.message)
                })
    }

    override fun onCleared() = disposable?.dispose() ?: Unit
}
