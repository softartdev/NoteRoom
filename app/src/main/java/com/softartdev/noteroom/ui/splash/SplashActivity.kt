package com.softartdev.noteroom.ui.splash

import android.content.Intent
import android.os.Bundle
import com.softartdev.noteroom.ui.base.BaseActivity
import com.softartdev.noteroom.ui.main.MainActivity
import com.softartdev.noteroom.ui.signin.SignInActivity
import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashView {
    @Inject lateinit var mPresenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent().inject(this)
        mPresenter.attachView(this)

        mPresenter.checkEncryption()
    }

    override fun navSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun navMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }
}
