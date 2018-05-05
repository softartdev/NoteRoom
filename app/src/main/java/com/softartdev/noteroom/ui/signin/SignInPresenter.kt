package com.softartdev.noteroom.ui.signin

import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.di.ConfigPersistent
import com.softartdev.noteroom.ui.base.BasePresenter
import javax.inject.Inject

@ConfigPersistent
class SignInPresenter @Inject
constructor(private val dataManager: DataManager) : BasePresenter<SignInView>() {

    override fun attachView(mvpView: SignInView) {
        super.attachView(mvpView)
    }

    fun signIn(pass: String) {
        mvpView!!.hideError()
        if (pass.isEmpty()) {
            mvpView!!.showEmptyPassError()
        } else if (checkPass(pass)) {
            mvpView!!.navMain()
        } else {
            mvpView!!.showIncorrectPassError()
        }
    }

    private fun checkPass(pass: String): Boolean {
        return dataManager.checkPass(pass)
    }
}
