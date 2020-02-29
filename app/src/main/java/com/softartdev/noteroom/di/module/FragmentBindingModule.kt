package com.softartdev.noteroom.di.module

import com.softartdev.noteroom.di.CryptScope
import com.softartdev.noteroom.di.NoteScope
import com.softartdev.noteroom.ui.settings.SettingsFragment
import com.softartdev.noteroom.ui.settings.security.change.ChangePasswordDialog
import com.softartdev.noteroom.ui.settings.security.confirm.ConfirmPasswordDialog
import com.softartdev.noteroom.ui.settings.security.enter.EnterPasswordDialog
import com.softartdev.noteroom.ui.title.EditTitleDialog
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBindingModule {

    @NoteScope
    @ContributesAndroidInjector
    abstract fun editTitleDialog(): EditTitleDialog

    @CryptScope
    @ContributesAndroidInjector
    abstract fun settingsFragment(): SettingsFragment

    @CryptScope
    @ContributesAndroidInjector
    abstract fun enterPasswordDialog(): EnterPasswordDialog

    @CryptScope
    @ContributesAndroidInjector
    abstract fun confirmPasswordDialog(): ConfirmPasswordDialog

    @CryptScope
    @ContributesAndroidInjector
    abstract fun changePasswordDialog(): ChangePasswordDialog

}