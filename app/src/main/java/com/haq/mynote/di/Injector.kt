package com.haq.mynote.di

import android.content.Context

class Injector private constructor() {

    lateinit var appComponent: AppComponent
        private set

    fun createAppComponent(@AppContext context: Context) {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(context))
                .build()
    }

    companion object {
        val instance by lazy {
            Injector()
        }
    }
}