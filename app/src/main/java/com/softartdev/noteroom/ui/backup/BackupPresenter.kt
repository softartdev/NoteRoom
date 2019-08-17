package com.softartdev.noteroom.ui.backup

import com.crashlytics.android.Crashlytics
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.di.ConfigPersistent
import com.softartdev.noteroom.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ConfigPersistent
class BackupPresenter @Inject constructor(
        private val dataManager: DataManager
) : BasePresenter<BackupView>() {

    fun backup() {
        checkViewAttached()
        addDisposable(dataManager.isEncryption()
                .delay(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mvpView?.showProgress(true) }
                .doAfterTerminate { mvpView?.showProgress(false) }
                .subscribe({ isEncryption ->
                    mvpView?.showBackup(isEncryption)
                }, { throwable ->
                    Crashlytics.logException(throwable)
                    mvpView?.showError(throwable)
                }))
    }
}