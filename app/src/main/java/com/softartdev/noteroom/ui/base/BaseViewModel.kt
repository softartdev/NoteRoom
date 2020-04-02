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

    abstract val resultFactory: ResultFactory<T>

    fun launch(
            useIdling: Boolean = true,
            block: suspend CoroutineScope.() -> T
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            wrapEspressoIdlingResource(useIdling) {
                if (useIdling) {
                    resultFactory.loadingResult?.let { loading -> onResult(loading) }
                }
                val result: T = try {
                    block()
                } catch (e: Throwable) {
                    Timber.e(e)
                    resultFactory.errorResult(e)
                }
                onResult(result)
            }
        }
    }

    private suspend inline fun onResult(result: T) = withContext(Dispatchers.Main) {
        resultLiveData.value = result
    }
}