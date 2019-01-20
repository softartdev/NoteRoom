package com.softartdev.noteroom.ui.splash

import com.crashlytics.android.Crashlytics
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.di.ConfigPersistent
import com.softartdev.noteroom.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@ConfigPersistent
class SplashPresenter @Inject constructor(
        private val dataManager: DataManager
) : BasePresenter<SplashView>() {

    fun checkEncryption() {
        checkViewAttached()
        addDisposable(dataManager.isEncryption()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ isEncrypted ->
                    if (isEncrypted) {
                        mvpView?.navSignIn()
                    } else {
                        mvpView?.navMain()
                    }
                }, { throwable ->
                    Crashlytics.logException(throwable)
                    throwable.printStackTrace()
                    mvpView?.showError(throwable.message)
                }))
    }
}
