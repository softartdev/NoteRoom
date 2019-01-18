package com.softartdev.noteroom.di.module

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.softartdev.noteroom.di.ActivityContext
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private val mFragment: Fragment) {

    @Provides
    internal fun providesFragment(): Fragment {
        return mFragment
    }

    @Provides
    internal fun provideActivity(): Activity {
        return mFragment.activity as Activity
    }

    @Provides
    @ActivityContext
    internal fun providesContext(): Context {
        return mFragment.activity as Context
    }

}