package com.haq.mynote.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.haq.mynote.data.NoteEntity

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class MyNoteDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object {
        const val DATABASE_NAME = "note_database"
    }
}