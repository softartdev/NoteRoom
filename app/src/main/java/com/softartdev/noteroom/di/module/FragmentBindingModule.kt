package com.softartdev.noteroom.di.module

import com.softartdev.noteroom.ui.security.change.ChangePasswordDialog
import com.softartdev.noteroom.ui.security.confirm.ConfirmPasswordDialog
import com.softartdev.noteroom.ui.security.enter.EnterPasswordDialog
import com.softartdev.noteroom.ui.settings.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract fun settingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun enterPasswordDialog(): EnterPasswordDialog

    @ContributesAndroidInjector
    abstract fun confirmPasswordDialog(): ConfirmPasswordDialog

    @ContributesAndroidInjector
    abstract fun changePasswordDialog(): ChangePasswordDialog

}