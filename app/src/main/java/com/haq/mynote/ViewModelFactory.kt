package com.haq.mynote

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.haq.mynote.data.source.NoteRepository
import com.haq.mynote.ui.notelist.NoteListViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(NoteListViewModel::class.java) ->
                    NoteListViewModel(noteRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}