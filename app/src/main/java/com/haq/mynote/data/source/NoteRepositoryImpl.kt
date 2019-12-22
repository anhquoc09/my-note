package com.haq.mynote.data.source

import android.arch.lifecycle.LiveData
import com.haq.mynote.data.NoteEntity
import com.haq.mynote.data.Result

class NoteRepositoryImpl(private val noteLocalDataSource: NoteDataSource) :
    NoteRepository {

    override val allNotes: LiveData<List<NoteEntity>> = noteLocalDataSource.getNotes()

    override suspend fun getNote(noteId: String): Result<NoteEntity> =
        noteLocalDataSource.getNote(noteId)

    override suspend fun saveNote(note: NoteEntity) =
        noteLocalDataSource.saveNote(note)

    override suspend fun deleteNote(noteId: String) =
        noteLocalDataSource.deleteNote(noteId)
}