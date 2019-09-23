package com.softartdev.noteroom.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

/**
 * Extension method to provide show keyboard for View.
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

/**
 * Extension method to provide show keyboard for View.
 */
fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

/**
 * Extension method to provide show keyboard for View.
 */
fun View.visible() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

fun MenuItem.tintIcon(
        context: Context,
        @ColorInt color: Int = getThemeColor(context, android.R.attr.textColorPrimary)
) {
    val drawableWrap = DrawableCompat.wrap(icon).mutate()
    DrawableCompat.setTint(drawableWrap, color)
    icon = drawableWrap
}

fun TextView.tintLeftDrawable(@DrawableRes drawableRes: Int) {
    val drawable = ContextCompat.getDrawable(context, drawableRes)
    val tint = getThemeColor(context, android.R.attr.textColorPrimary)
    if (drawable != null) {
        DrawableCompat.setTint(drawable, tint)
    }
    setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
}

/**
 * Queries the theme of the given `context` for a theme color.
 *
 * @param context   the context holding the current theme.
 * @param attrResId the theme color attribute to resolve.
 * @return the theme color
 */
@ColorInt
fun getThemeColor(context: Context, @AttrRes attrResId: Int): Int {
    val a = context.obtainStyledAttributes(null, intArrayOf(attrResId))
    try {
        return a.getColor(0, Color.MAGENTA)
    } finally {
        a.recycle()
    }
}
