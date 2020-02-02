package com.softartdev.noteroom.di.component

import com.softartdev.noteroom.NoteRoomApp
import com.softartdev.noteroom.di.module.ActivityBindingModule
import com.softartdev.noteroom.di.module.AppModule
import com.softartdev.noteroom.di.module.DataModule
import com.softartdev.noteroom.di.module.FragmentBindingModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityBindingModule::class,
    FragmentBindingModule::class,
    DataModule::class])
interface AppComponent : AndroidInjector<NoteRoomApp> {

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<NoteRoomApp>
}
