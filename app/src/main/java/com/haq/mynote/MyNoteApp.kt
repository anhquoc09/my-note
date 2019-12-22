package com.haq.mynote

import android.app.Application
import com.haq.mynote.di.Injector

class MyNoteApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        Injector.instance.createAppComponent(this)
    }

    companion object {
        lateinit var instance: MyNoteApp
            private set
    }
}