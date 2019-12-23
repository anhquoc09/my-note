package com.haq.mynote.ui

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class BaseViewModel : ViewModel() {
    private val supervisorJob = SupervisorJob()

    protected val supervisorScope = CoroutineScope(Dispatchers.Main + supervisorJob)

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        supervisorJob.cancel()
    }
}