package com.softartdev.noteroom.ui.security

import android.text.Editable
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.softartdev.noteroom.R

class PassMediator (
        private val textInputLayout: TextInputLayout,
        private val editText: EditText
) : DialogDirector {

    override val textString: Editable
        get() = editText.text

    override fun showIncorrectPasswordError() = with(textInputLayout) {
        error = context.getString(R.string.incorrect_password)
    }

    override fun showEmptyPasswordError() = with(textInputLayout) {
        error = context.getString(R.string.empty_password)
    }

    override fun showPasswordsNoMatchError() = with(textInputLayout) {
        error = context.getString(R.string.passwords_do_not_match)
    }

    override fun hideError() = with(textInputLayout) {
        error = null
    }
}