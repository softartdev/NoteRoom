package com.softartdev.noteroom

import android.app.Application
import android.content.Context
import com.softartdev.noteroom.di.component.ApplicationComponent
import com.softartdev.noteroom.di.component.DaggerApplicationComponent
import com.softartdev.noteroom.di.module.ApplicationModule
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

class App : Application() {

    private var mApplicationComponent: ApplicationComponent? = null

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            LeakCanary.install(this)
        }
    }

    // Needed to replace the component with a test specific one
    var component: ApplicationComponent
        get() {
            if (mApplicationComponent == null) {
                mApplicationComponent = DaggerApplicationComponent.builder()
                        .applicationModule(ApplicationModule(this))
                        .build()
            }
            return mApplicationComponent as ApplicationComponent
        }
        set(applicationComponent) {
            mApplicationComponent = applicationComponent
        }

    companion object {

        operator fun get(context: Context): App {
            return context.applicationContext as App
        }
    }
}
