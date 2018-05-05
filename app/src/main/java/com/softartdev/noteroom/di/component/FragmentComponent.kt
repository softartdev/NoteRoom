package com.softartdev.noteroom.di.component

import com.softartdev.noteroom.di.PerFragment
import com.softartdev.noteroom.di.module.FragmentModule
import dagger.Subcomponent

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = arrayOf(FragmentModule::class))
interface FragmentComponent