package com.softartdev.noteroom.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softartdev.noteroom.util.EspressoIdlingResource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseViewModel<T : Any> : ViewModel() {

//    val resultLiveData = MutableLiveData<T>()
    private val _stateFlow = MutableStateFlow<T?>(null)
    private val stateFlow: StateFlow<T?> get() = _stateFlow
    val flow: Flow<T> get() = _stateFlow.filterNotNull()

    open val loadingResult: T? = null

    fun launch(
            useIdling: Boolean = true,
            block: suspend CoroutineScope.() -> T
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (useIdling) {
                    EspressoIdlingResource.increment()
                    loadingResult?.let { loading -> onResult(loading) }
                }
                onResult(block())
            } catch (e: Throwable) {
                Timber.e(e)
                onResult(errorResult(e))
            } finally {
                if (useIdling) EspressoIdlingResource.decrement()
            }
        }.start()
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
        _stateFlow.value = result
    }

    abstract fun errorResult(throwable: Throwable): T
}