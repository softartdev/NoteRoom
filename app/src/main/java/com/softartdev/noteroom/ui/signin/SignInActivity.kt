package com.softartdev.noteroom.ui.signin

import android.content.Intent
import android.os.Bundle
import com.softartdev.noteroom.R
import com.softartdev.noteroom.ui.base.BaseActivity
import com.softartdev.noteroom.ui.main.MainActivity
import io.github.tonnyl.spark.Spark
import kotlinx.android.synthetic.main.activity_sign_in.*
import javax.inject.Inject

class SignInActivity : BaseActivity(), SignInView {
    @Inject lateinit var mPresenter: SignInPresenter

    private lateinit var mSpark: Spark

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        activityComponent().inject(this)
        mPresenter.attachView(this)

        password_edit_text.setOnEditorActionListener { _, _, _ ->
            attemptSignIn()
            true
        }
        sign_in_button.setOnClickListener { attemptSignIn() }

        mSpark = Spark.Builder()
                .setView(sign_in_layout) // View or view group
                .setAnimList(Spark.ANIM_GREEN_PURPLE)
                .build()
    }

    override fun onResume() {
        super.onResume()
        mSpark.startAnimation()
    }

    override fun onPause() {
        super.onPause()
        mSpark.stopAnimation()
    }

    private fun attemptSignIn() {
        mPresenter.signIn(password_edit_text?.text.toString())
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

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }
}

