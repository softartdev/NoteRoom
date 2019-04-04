package com.softartdev.noteroom

import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.softartdev.noteroom.di.component.ApplicationComponent
import com.softartdev.noteroom.di.component.DaggerApplicationComponent
import com.softartdev.noteroom.di.module.ApplicationModule
import com.squareup.leakcanary.LeakCanary
import io.fabric.sdk.android.Fabric
import timber.log.Timber

class App : Application() {

    private var mApplicationComponent: ApplicationComponent? = null

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            LeakCanary.install(this)
        }
        val crashlyticsCore = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        val crashlytics = Crashlytics.Builder().core(crashlyticsCore).build()
        Fabric.with(this, crashlytics)
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
        operator fun get(context: Context): App = context.applicationContext as App
    }
}
