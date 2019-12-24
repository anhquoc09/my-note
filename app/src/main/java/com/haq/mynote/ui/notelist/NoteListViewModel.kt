package com.haq.mynote.ui.notelist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.haq.mynote.data.NoteEntity
import com.haq.mynote.data.Result
import com.haq.mynote.data.source.NoteRepository
import com.haq.mynote.ui.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class NoteListViewModel(private val noteRepository: NoteRepository) : BaseViewModel() {

    private val allNote = noteRepository.allNotes

    private var filterKey = MutableLiveData<String>()

    private var selectedNote = MutableLiveData<NoteUIModel>()

    private val titleLiveData = MutableLiveData<String>()

    private val contentLiveData = MutableLiveData<String>()

    private val _state = MediatorLiveData<NotesState>().apply {
        value = NotesState(
            false,
            allNote.value?.map { transform(it) }.orEmpty(),
            selectedNote.value,
            null
        )

        addSource(allNote) {
            postValue(combineData())
        }

        addSource(filterKey) {
            postValue(combineData())
        }

        addSource(selectedNote) {
            postValue(combineData())
        }
    }
    val state: LiveData<NotesState>
        get() = _state

    @Synchronized
    private fun combineData(): NotesState =
        NotesState(isLoading = false,
            noteList = allNote.value
                ?.filter {
                    it.title.trim().contains(filterKey.value.orEmpty(), true)
                }
                ?.map { transform(it) }.orEmpty(),
            selectedNote = selectedNote.value
        )

    private fun transform(entity: NoteEntity) = NoteUIModel(
        entity.id,
        entity.title,
        entity.content,
        entity.createTime,
        filterKey.value.orEmpty(),
        entity.id == selectedNote.value?.id
    )

    private fun saveNote(note: NoteUIModel) {
        GlobalScope.launch {
            noteRepository.saveNote(
                NoteEntity(
                    note.id,
                    note.title,
                    note.content,
                    note.createTime
                )
            )
        }
    }

    fun filter(filter: String) {
        filterKey.value = filter.trim()
    }

    fun selectNote(noteId: String) {
        saveNote()
        if (noteId != selectedNote.value?.id) {
            selectedNote.value =
                allNote.value?.firstOrNull { it.id == noteId }?.run { transform(this) }
        }
    }

    fun newNote() {
        saveNote()
        selectedNote.value = null
    }

    fun saveNote() {
        selectedNote.value?.let {
            saveNote(it.apply {
                this.title = titleLiveData.value.orEmpty()
                this.content = contentLiveData.value.orEmpty()
            })
        } ?: let {
            if (!titleLiveData.value.isNullOrEmpty() || !contentLiveData.value.isNullOrEmpty())
                saveNote(
                    NoteUIModel(
                        UUID.randomUUID().toString(),
                        titleLiveData.value.orEmpty(),
                        contentLiveData.value.orEmpty(),
                        System.currentTimeMillis()
                    )
                )
        }
    }

    fun deleteSelectedNote() {
        _state.value = _state.value?.copy(isLoading = true)
        supervisorScope.launch {
            selectedNote.value?.let {
                when (val result = noteRepository.deleteNote(it.id)) {
                    is Result.Success -> {
                        selectedNote.value = null
                    }
                    is Result.Error -> {
                        _state.value =
                            _state.value?.copy(isLoading = false, error = result.exception)
                    }
                }
            }
        }
    }

    fun updateTitle(title: String) {
        titleLiveData.value = title
    }

    fun updateContent(content: String) {
        contentLiveData.value = content
    }
}