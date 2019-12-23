package com.softartdev.noteroom.ui.security.enter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.noteroom.data.DataManager
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class EnterViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val enterLiveData = MutableLiveData<EnterResult>()

    private val compositeDisposable = CompositeDisposable()

    fun check(pass: String) {

    }

    override fun onCleared() = compositeDisposable.clear()

}