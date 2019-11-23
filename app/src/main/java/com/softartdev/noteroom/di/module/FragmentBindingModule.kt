package com.softartdev.noteroom.di.module

import com.softartdev.noteroom.ui.settings.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @ContributesAndroidInjector()
    abstract fun settingsFragment(): SettingsFragment

}