package com.softartdev.noteroom.di.component

import com.softartdev.noteroom.di.PerFragment
import com.softartdev.noteroom.di.module.FragmentModule
import com.softartdev.noteroom.ui.settings.SettingsFragment
import dagger.Subcomponent

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {
    fun inject(settingsFragment: SettingsFragment)
}