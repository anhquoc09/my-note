package com.haq.mynote.data.source.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.haq.mynote.data.NoteEntity

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER by create_time DESC")
    fun getNotes(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    fun getNoteById(noteId: String): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :noteId")
    fun deleteNoteById(noteId: String)
}