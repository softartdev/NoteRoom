package com.softartdev.noteroom.di

import com.softartdev.noteroom.shared.data.CryptUseCase
import com.softartdev.noteroom.shared.data.NoteUseCase
import com.softartdev.noteroom.shared.data.SafeRepo
import com.softartdev.noteroom.util.PreferencesHelper
import org.koin.dsl.module
import org.mockito.Mockito.mock

val testModule = module {
    single { mock(PreferencesHelper::class.java) }
    single { mock(SafeRepo::class.java) }
    single { CryptUseCase(get()) }
    single { NoteUseCase(get()) }
}