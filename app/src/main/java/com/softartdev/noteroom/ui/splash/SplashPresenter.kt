package com.softartdev.noteroom.ui.splash

import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.di.ConfigPersistent
import com.softartdev.noteroom.ui.base.BasePresenter
import javax.inject.Inject

@ConfigPersistent
class SplashPresenter @Inject
constructor(private val dataManager: DataManager) : BasePresenter<SplashView>() {

    fun checkEncryption() {
        checkViewAttached()
        if (dataManager.isEncryption()) {
            mvpView!!.navSignIn()
        } else {
            mvpView!!.navMain()
        }
    }
}
