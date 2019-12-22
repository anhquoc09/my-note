package com.haq.mynote.data.source

import android.arch.lifecycle.LiveData
import com.haq.mynote.data.NoteEntity
import com.haq.mynote.data.Result

interface NoteRepository {

    val allNotes: LiveData<List<NoteEntity>>

    suspend fun getNote(noteId: String): Result<NoteEntity>

    suspend fun saveNote(note: NoteEntity): Result<Boolean>

    suspend fun deleteNote(noteId: String): Result<Boolean>
}