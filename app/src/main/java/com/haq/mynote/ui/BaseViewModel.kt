package com.haq.mynote.ui

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {
    private val job = Job()

    protected val uiScope = CoroutineScope(Dispatchers.Main + job)

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}