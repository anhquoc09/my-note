package com.haq.mynote.di

import android.arch.persistence.room.Room
import android.content.Context
import com.haq.mynote.data.source.NoteRepositoryImpl
import com.haq.mynote.data.source.NoteDataSource
import com.haq.mynote.data.source.NoteRepository
import com.haq.mynote.data.source.local.NoteDao
import com.haq.mynote.data.source.local.MyNoteDatabase
import com.haq.mynote.data.source.local.MyNoteDatabase.Companion.DATABASE_NAME
import com.haq.mynote.data.source.local.NoteLocalDataSource
import dagger.Module
import dagger.Provides

@Module
class AppModule(context: Context) {

    private val appContext = context.applicationContext

    private val myNoteDatabase = Room.databaseBuilder(appContext, MyNoteDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @AppContext
    fun provideAppContext(): Context = appContext

    @Provides
    fun provideNoteDatabase(): MyNoteDatabase = myNoteDatabase

    @Provides
    fun provideNoteDao(): NoteDao = myNoteDatabase.getNoteDao()

    @Provides
    fun provideNoteLocalDataSource(noteDao: NoteDao): NoteDataSource = NoteLocalDataSource(noteDao)

    @Provides
    fun provideNoteRepository(localDataSource: NoteLocalDataSource): NoteRepository = NoteRepositoryImpl(localDataSource)
}