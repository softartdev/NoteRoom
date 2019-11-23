package com.softartdev.noteroom.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.softartdev.noteroom.model.SplashResult
import com.softartdev.noteroom.ui.base.BaseActivity
import com.softartdev.noteroom.ui.main.MainActivity
import com.softartdev.noteroom.ui.signin.SignInActivity

class SplashActivity : BaseActivity(), Observer<SplashResult> {
    private val splashViewModel by viewModels<SplashViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashViewModel.splashLiveData.observe(this, this)
    }

    override fun onChanged(splashResult: SplashResult) = when (splashResult) {
        SplashResult.NavSignIn -> navSignIn()
        SplashResult.NavMain -> navMain()
        is SplashResult.ShowError -> showError(splashResult.message)
    }

    private fun navSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showError(message: String?) {
        with(AlertDialog.Builder(this)) {
            setTitle(android.R.string.dialog_alert_title)
            setMessage(message)
            setNeutralButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
            show()
        }
    }
}
