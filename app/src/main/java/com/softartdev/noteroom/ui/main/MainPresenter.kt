package com.softartdev.noteroom.ui.main

import com.crashlytics.android.Crashlytics
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.di.ConfigPersistent
import com.softartdev.noteroom.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.sqlcipher.database.SQLiteException
import javax.inject.Inject

@ConfigPersistent
class MainPresenter @Inject constructor(
        private val dataManager: DataManager
) : BasePresenter<MainView>() {

    fun updateNotes() {
        checkViewAttached()
        mvpView?.showProgress(true)
        addDisposable(dataManager.notes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ notes ->
                    mvpView?.apply {
                        showProgress(false)
                        if (notes.isNotEmpty()) {
                            onUpdateNotes(notes)
                        } else {
                            showEmpty()
                        }
                    }
                }, { throwable ->
                    Crashlytics.logException(throwable)
                    mvpView?.apply {
                        showProgress(false)
                        if (throwable is SQLiteException) {
                            navSignIn()
                        } else {
                            showError(throwable)
                        }
                    }
                }))
    }
}
