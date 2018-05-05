package com.softartdev.noteroom.di.component

import com.softartdev.noteroom.di.ConfigPersistent
import com.softartdev.noteroom.di.module.ActivityModule
import com.softartdev.noteroom.di.module.FragmentModule
import com.softartdev.noteroom.ui.base.BaseActivity
import com.softartdev.noteroom.ui.base.BaseFragment
import dagger.Component

/**
 * A dagger component that will live during the lifecycle of an Activity or Fragment but it won't
 * be destroy during configuration changes. Check [BaseActivity] and [BaseFragment] to
 * see how this components survives configuration changes.
 * Use the [ConfigPersistent] scope to annotate dependencies that need to survive
 * configuration changes (for example Presenters).
 */
@ConfigPersistent
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface ConfigPersistentComponent {

    fun activityComponent(activityModule: ActivityModule): ActivityComponent

    fun fragmentComponent(fragmentModule: FragmentModule): FragmentComponent

}
