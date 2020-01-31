package com.softartdev.noteroom.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softartdev.noteroom.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                loadingResult?.let { loading ->
                    onResult(loading)
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

    private suspend inline fun onResult(result: T) = withContext(Dispatchers.Main) {
        resultLiveData.value = result
    }

    abstract fun errorResult(throwable: Throwable): T
}