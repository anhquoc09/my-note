package com.haq.mynote.di

import android.content.Context
import com.haq.mynote.data.source.NoteRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @AppContext
    fun getAppContext(): Context

    fun getRepository(): NoteRepository

    fun plusPresentationComponent(): PresentationComponent
}