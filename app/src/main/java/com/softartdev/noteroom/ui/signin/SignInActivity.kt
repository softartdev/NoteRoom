package com.softartdev.noteroom.ui.signin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.softartdev.noteroom.R
import com.softartdev.noteroom.model.SignInResult
import com.softartdev.noteroom.ui.base.BaseActivity
import com.softartdev.noteroom.ui.main.MainActivity
import com.softartdev.noteroom.util.gone
import com.softartdev.noteroom.util.hideKeyboard
import com.softartdev.noteroom.util.visible
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.view_error.view.*

class SignInActivity : BaseActivity(), Observer<SignInResult> {
    private val signInViewModel by viewModels<SignInViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        sign_in_error_view.button_reload.setOnClickListener { attemptSignIn() }

        password_edit_text.setOnEditorActionListener { _, _, _ ->
            attemptSignIn()
            true
        }
        sign_in_button.setOnClickListener { attemptSignIn() }

        signInViewModel.signInLiveData.observe(this, this)
    }

    private fun attemptSignIn() {
        hideKeyboard()
        val passphrase: Editable = password_edit_text.editableText
        signInViewModel.signIn(passphrase)
    }

    override fun onChanged(signInResult: SignInResult) = when (signInResult) {
        SignInResult.NavMain -> navMain()
        SignInResult.ShowEmptyPassError -> showEmptyPassError()
        SignInResult.ShowIncorrectPassError -> showIncorrectPassError()
        SignInResult.HideError -> hideError()
        is SignInResult.ShowProgress -> showProgress(signInResult.show)
        is SignInResult.ShowError -> showError(signInResult.error)
    }

    private fun navMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun hideError() {
        password_text_input_layout?.error = null
    }

    private fun showEmptyPassError() {
        password_text_input_layout?.error = getString(R.string.empty_password)
    }

    private fun showIncorrectPassError() {
        password_text_input_layout?.error = getString(R.string.incorrect_password)
    }

    private fun showProgress(show: Boolean) {
        sign_in_progress_view.apply { if (show) visible() else gone() }
    }

    private fun showError(error: Throwable) {
        sign_in_error_view.apply {
            visible()
            text_error_message.text = error.message
        }
    }
}

