package com.softartdev.noteroom.ui.security.change

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.noteroom.data.DataManager
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ChangeViewModel @Inject constructor(
        private val dataManager: DataManager
) : ViewModel() {

    val changeLiveData = MutableLiveData<ChangeViewModel>()

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() = compositeDisposable.clear()

}