package com.softartdev.noteroom.ui.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.softartdev.noteroom.ui.base.BaseActivity
import com.softartdev.noteroom.ui.main.MainActivity
import com.softartdev.noteroom.ui.signin.SignInActivity
import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashView {
    @Inject lateinit var splashPresenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent().inject(this)
        splashPresenter.attachView(this)

        splashPresenter.checkEncryption()
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

    override fun showError(message: String?) {
        with(AlertDialog.Builder(this)) {
            setTitle(android.R.string.dialog_alert_title)
            setMessage(message)
            setNeutralButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
            show()
        }
    }

    override fun onDestroy() {
        splashPresenter.detachView()
        super.onDestroy()
    }
}
