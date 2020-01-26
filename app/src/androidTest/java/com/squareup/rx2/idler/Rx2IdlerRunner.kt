package com.squareup.rx2.idler

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.runner.AndroidJUnitRunner
import io.reactivex.Scheduler
import io.reactivex.functions.Function
import io.reactivex.plugins.RxJavaPlugins
import java.util.concurrent.Callable

class Rx2IdlerRunner : AndroidJUnitRunner(), IdlingResource.ResourceCallback {

    override fun onStart() {
        val compDelegateIdlingScheduler = create("RxJava 2.x Computation Scheduler")
        RxJavaPlugins.setInitComputationSchedulerHandler(compDelegateIdlingScheduler)

        val ioDelegateIdlingScheduler = create("RxJava 2.x IO Scheduler")
        RxJavaPlugins.setInitIoSchedulerHandler(ioDelegateIdlingScheduler)

        super.onStart()
    }

    private fun create(name: String): Function<Callable<Scheduler>, Scheduler> = Function { delegate ->
        val scheduler: IdlingResourceScheduler = FixedDelegatingIdlingResourceScheduler(delegate.call(), name)
        IdlingRegistry.getInstance().register(scheduler)
        scheduler
    }

    override fun onTransitionToIdle() = Unit

    override fun onDestroy() {
        val idlingRegistry = IdlingRegistry.getInstance()
        idlingRegistry.unregister(*idlingRegistry.resources.toTypedArray())

        super.onDestroy()
    }
}
