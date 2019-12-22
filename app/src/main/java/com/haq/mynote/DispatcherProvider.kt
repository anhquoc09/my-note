package com.haq.mynote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

class DispatcherProvider private constructor() {
    companion object {
        val io = Dispatchers.IO
        val main = Dispatchers.Main
        val default = Dispatchers.Default
        val singleThread = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }
}