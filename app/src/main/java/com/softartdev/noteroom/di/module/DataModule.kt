package com.softartdev.noteroom.di.module

import android.content.Context
import com.softartdev.noteroom.data.CryptUseCase
import com.softartdev.noteroom.data.NoteUseCase
import com.softartdev.noteroom.data.SafeRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    internal fun provideSafeRepo(context: Context): SafeRepo {
        return SafeRepo(context)
    }

    @Provides
    @Singleton
    internal fun provideCryptUseCase(context: Context, safeRepo: SafeRepo): CryptUseCase {
        return CryptUseCase(context, safeRepo)
    }

    @Provides
    @Singleton
    internal fun provideNoteUseCase(safeRepo: SafeRepo): NoteUseCase {
        return NoteUseCase(safeRepo)
    }
}
