package com.softartdev.noteroom.ui.signin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import com.softartdev.noteroom.R
import com.softartdev.noteroom.ui.base.BaseActivity
import com.softartdev.noteroom.ui.common.OnReloadClickListener
import com.softartdev.noteroom.ui.main.MainActivity
import com.softartdev.noteroom.util.gone
import com.softartdev.noteroom.util.hideKeyboard
import com.softartdev.noteroom.util.visible
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.view_error.*
import timber.log.Timber
import javax.inject.Inject

class SignInActivity : BaseActivity(), SignInView, OnReloadClickListener {
    @Inject lateinit var signInPresenter: SignInPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        activityComponent().inject(this)
        signInPresenter.attachView(this)

        sign_in_error_view.reloadClickListener = this

        password_edit_text.setOnEditorActionListener { _, _, _ ->
            attemptSignIn()
            true
        }
        sign_in_button.setOnClickListener { attemptSignIn() }
    }

    private fun attemptSignIn() {
        hideKeyboard()
        val passphrase: Editable = password_edit_text.editableText
        signInPresenter.signIn(passphrase)
    }

    override fun navMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun hideError() {
        password_text_input_layout?.error = null
    }

    override fun showEmptyPassError() {
        password_text_input_layout?.error = getString(R.string.empty_password)
    }

    override fun showIncorrectPassError() {
        password_text_input_layout?.error = getString(R.string.incorrect_password)
    }

    override fun showProgress(show: Boolean) {
        sign_in_progress_view.apply { if (show) visible() else gone() }
    }

    override fun showError(error: Throwable) {
        sign_in_error_view.apply {
            visible()
            text_error_message.text = error.message
        }
        Timber.e(error, "There was an error sign in")
    }

    override fun onReloadClick() = attemptSignIn()

    override fun onDestroy() {
        signInPresenter.detachView()
        super.onDestroy()
    }
}

