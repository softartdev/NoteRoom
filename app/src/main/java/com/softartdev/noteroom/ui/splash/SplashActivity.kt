package com.softartdev.noteroom.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.softartdev.noteroom.ui.main.MainActivity
import com.softartdev.noteroom.ui.signin.SignInActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.scope.viewModel

class SplashActivity : AppCompatActivity(), Observer<SplashResult> {
    private val splashViewModel by lifecycleScope.viewModel<SplashViewModel>(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashViewModel.viewModelScope.launch(Dispatchers.Main) { splashViewModel.flow.collect { onChanged(it) } }
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
