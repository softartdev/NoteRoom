package com.softartdev.noteroom.ui.signin

import android.text.Editable
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.di.ConfigPersistent
import com.softartdev.noteroom.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@ConfigPersistent
class SignInPresenter @Inject
constructor(private val dataManager: DataManager) : BasePresenter<SignInView>() {

    fun signIn(pass: Editable) {
        checkViewAttached()
        mvpView?.hideError()
        if (pass.isEmpty()) {
            mvpView?.showEmptyPassError()
        } else {
            mvpView?.showProgress(true)
            addDisposable(dataManager.checkPass(pass)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ checked ->
                        mvpView?.apply {
                            showProgress(false)
                            if (checked) {
                                navMain()
                            } else {
                                showIncorrectPassError()
                            }
                        }
                    }, { throwable ->
                        mvpView?.apply {
                            showProgress(false)
                            showError(throwable)
                        }
                    }))
        }
    }
}
