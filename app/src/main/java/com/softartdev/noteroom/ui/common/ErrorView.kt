package com.softartdev.noteroom.ui.common

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.softartdev.noteroom.R
import kotlinx.android.synthetic.main.view_error.view.*

class ErrorView : LinearLayout {

    var reloadClickListener: OnReloadClickListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
        LayoutInflater.from(context).inflate(R.layout.view_error, this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            background = ColorDrawable(ContextCompat.getColor(context, R.color.translucent))
        } else {
            setBackgroundColor(ContextCompat.getColor(context, R.color.translucent))
        }
        button_reload.setOnClickListener { reloadClickListener?.onReloadClick() }
        button_cancel.setOnClickListener { this@ErrorView.visibility = View.GONE }
    }

    fun setOnReloadClickListener(reloadListener: () -> Unit) {
        reloadClickListener = object : OnReloadClickListener {
            override fun onReloadClick() {
                reloadListener()
            }
        }
    }
}
