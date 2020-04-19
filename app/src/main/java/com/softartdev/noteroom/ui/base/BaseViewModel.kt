package com.softartdev.noteroom.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softartdev.noteroom.util.EspressoIdlingResource
import com.softartdev.noteroom.util.wrapEspressoIdlingResource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

abstract class BaseViewModel<T> : ViewModel() {

    val resultLiveData = MutableLiveData<T>()

    open val loadingResult: T? = null

    fun launch(
            useIdling: Boolean = true,
            block: suspend CoroutineScope.() -> T
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            wrapEspressoIdlingResource(useIdling) {
                if (useIdling) {
                    loadingResult?.let { loading -> onResult(loading) }
                }
                val result: T = try {
                    block()
                } catch (e: Throwable) {
                    Timber.e(e)
                    errorResult(e)
                }
                onResult(result)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun launch(flow: Flow<T>) {
        viewModelScope.launch(Dispatchers.IO) {
            flow.onStart {
                loadingResult ?: return@onStart
                EspressoIdlingResource.increment()
                emit(loadingResult!!)
            }.onEach { result ->
                onResult(result)
                if (result == loadingResult) EspressoIdlingResource.decrement()
            }.catch { throwable ->
                onResult(errorResult(throwable))
            }.launchIn(this)
        }.start()
    }

    private suspend inline fun onResult(result: T) = withContext(Dispatchers.Main) {
        resultLiveData.value = result
    }

    abstract fun errorResult(throwable: Throwable): T
}