package com.softartdev.noteroom.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.softartdev.noteroom.R
import kotlinx.android.synthetic.main.view_error.view.*

class ErrorView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var reloadClickListener: OnReloadClickListener? = null

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        LayoutInflater.from(context).inflate(R.layout.view_error, this)
        button_reload.setOnClickListener { reloadClickListener?.onReloadClick() }
        button_cancel.setOnClickListener { this@ErrorView.visibility = View.GONE }
    }
}
