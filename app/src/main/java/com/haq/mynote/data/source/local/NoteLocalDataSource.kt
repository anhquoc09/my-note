package com.haq.mynote.data.source.local

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.haq.mynote.DispatcherProvider
import com.haq.mynote.data.NoteEntity
import com.haq.mynote.data.Result.Error
import com.haq.mynote.data.Result.Success
import com.haq.mynote.data.source.NoteDataSource
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteLocalDataSource @Inject constructor(private val noteDao: NoteDao) : NoteDataSource {

//    override fun getNotes(): LiveData<List<NoteEntity>> = noteDao.getNotes()

    override fun getNotes(): LiveData<List<NoteEntity>> =
        MutableLiveData<List<NoteEntity>>().apply {
            postValue(
                mutableListOf(
                    NoteEntity("1", "a", "b", System.currentTimeMillis()),
                    NoteEntity("2", "aa", "bb", System.currentTimeMillis()),
                    NoteEntity("3", "aaa", "bbb", System.currentTimeMillis()),
                    NoteEntity("4", "aaaa", "bbbb", System.currentTimeMillis()),
                    NoteEntity("5", "aaaa", "bbbb", System.currentTimeMillis()),
                    NoteEntity("6", "aaaa", "bbbb", System.currentTimeMillis()),
                    NoteEntity("7", "aaaa", "bbbb", System.currentTimeMillis()),
                    NoteEntity("8", "aaaa", "bbbb", System.currentTimeMillis()),
                    NoteEntity("9", "aaaa", "bbbb", System.currentTimeMillis()),
                    NoteEntity("10", "aaaa", "bbbb", System.currentTimeMillis())
                )
            )
        }

    override suspend fun getNote(noteId: String) =
        withContext(DispatcherProvider.io) {
            try {
                val note = noteDao.getNoteById(noteId)
                if (note != null) {
                    Success(note)
                } else {
                    Error(Exception("Note not found!"))
                }
            } catch (e: Exception) {
                Error(e)
            }
        }

    override suspend fun saveNote(note: NoteEntity) =
        withContext(DispatcherProvider.singleThread) {
            try {
                noteDao.insertNote(note)
                Success(true)
            } catch (e: Exception) {
                Error(e)
            }
        }

    override suspend fun deleteNote(noteId: String) =
        withContext(DispatcherProvider.singleThread) {
            try {
                noteDao.deleteNoteById(noteId)
                Success(true)
            } catch (e: Exception) {
                Error(e)
            }
        }
}