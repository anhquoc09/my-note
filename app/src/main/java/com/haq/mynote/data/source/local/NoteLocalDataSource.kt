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

    override fun getNotes(): LiveData<List<NoteEntity>> = noteDao.getNotes()

//    override fun getNotes(): LiveData<List<NoteEntity>> =
//        MutableLiveData<List<NoteEntity>>().apply {
//            postValue(
//                mutableListOf(
//                    NoteEntity("1", "Hoang Anh Quốc", "b", System.currentTimeMillis()),
//                    NoteEntity("2", "Hoang Anh Quốca", "bb", System.currentTimeMillis()),
//                    NoteEntity("3", "Hoang Anh Quốcaa", "bbb", System.currentTimeMillis()),
//                    NoteEntity("4", "Hoang Anh Quốcaaa", "bbbb", System.currentTimeMillis()),
//                    NoteEntity("5", "Hoang Anh Quốcaaa", "bbbb", System.currentTimeMillis()),
//                    NoteEntity("6", "Hoang Anh Quốcaaa", "bbbb", System.currentTimeMillis()),
//                    NoteEntity("7", "Hoang Anh Quốcaaa", "bbbb", System.currentTimeMillis()),
//                    NoteEntity("8", "Hoang Anh Quốcaaa", "bbbb", System.currentTimeMillis()),
//                    NoteEntity("9", "Hoang Anh Quốcaaa", "bbbb", System.currentTimeMillis()),
//                    NoteEntity("10", "Hoang Anh Quốcaaa", "bbbb", System.currentTimeMillis())
//                )
//            )
//        }

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