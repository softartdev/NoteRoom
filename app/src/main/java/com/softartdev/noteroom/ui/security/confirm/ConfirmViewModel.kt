package com.softartdev.noteroom.ui.security.confirm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.noteroom.data.DataManager
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ConfirmViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val confirmLiveData = MutableLiveData<ConfirmResult>()

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() = compositeDisposable.clear()

}