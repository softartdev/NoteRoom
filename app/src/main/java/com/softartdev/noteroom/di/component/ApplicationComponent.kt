package com.softartdev.noteroom.di.component

import android.app.Application
import android.content.Context
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.db.DbStore
import com.softartdev.noteroom.di.ApplicationContext
import com.softartdev.noteroom.di.module.ApplicationModule
import com.softartdev.noteroom.util.PreferencesHelper
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    @ApplicationContext
    fun context(): Context

    fun application(): Application

    fun preferencesHelper(): PreferencesHelper

    fun dbStore(): DbStore

    fun dataManager(): DataManager
}
