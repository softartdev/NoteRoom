package com.softartdev.noteroom.ui.signin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.softartdev.noteroom.R
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

        signInViewModel.resultLiveData.observe(this, this)
    }

    private fun attemptSignIn() {
        hideKeyboard()
        val passphrase: Editable = password_edit_text.editableText
        signInViewModel.signIn(passphrase)
    }

    override fun onChanged(signInResult: SignInResult) = when (signInResult) {
        is SignInResult.ShowProgress -> {
            sign_in_progress_view.visible()
            sign_in_layout.gone()
            sign_in_error_view.gone()
        }
        SignInResult.NavMain -> {
            showSignIn()
            navMain()
        }
        SignInResult.ShowEmptyPassError -> showSignIn(
                errorText = getString(R.string.empty_password)
        )
        SignInResult.ShowIncorrectPassError -> showSignIn(
                errorText = getString(R.string.incorrect_password)
        )
        is SignInResult.ShowError -> {
            sign_in_progress_view.gone()
            sign_in_layout.gone()
            sign_in_error_view.apply {
                text_error_message.text = signInResult.error.message
            }.visible()
        }
    }

    private fun showSignIn(errorText: String? = null) {
        sign_in_progress_view.gone()
        sign_in_layout.apply {
            password_text_input_layout.error = errorText
        }.visible()
        sign_in_error_view.gone()
    }

    private fun navMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}

