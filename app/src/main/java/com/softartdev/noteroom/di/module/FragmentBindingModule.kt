package com.softartdev.noteroom.di.module

import com.softartdev.noteroom.ui.settings.security.change.ChangePasswordDialog
import com.softartdev.noteroom.ui.settings.security.confirm.ConfirmPasswordDialog
import com.softartdev.noteroom.ui.settings.security.enter.EnterPasswordDialog
import com.softartdev.noteroom.ui.settings.SettingsFragment
import com.softartdev.noteroom.ui.title.EditTitleDialog
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract fun editTitleDialog(): EditTitleDialog

    @ContributesAndroidInjector
    abstract fun settingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun enterPasswordDialog(): EnterPasswordDialog

    @ContributesAndroidInjector
    abstract fun confirmPasswordDialog(): ConfirmPasswordDialog

    @ContributesAndroidInjector
    abstract fun changePasswordDialog(): ChangePasswordDialog

}