package com.softartdev.noteroom.ui.base

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


abstract class BasePrefFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }
}