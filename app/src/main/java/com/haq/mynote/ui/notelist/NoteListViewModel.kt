package com.haq.mynote.ui.notelist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.haq.mynote.data.NoteEntity
import com.haq.mynote.data.Result
import com.haq.mynote.data.source.NoteRepository
import com.haq.mynote.stripAccents
import com.haq.mynote.ui.BaseViewModel
import kotlinx.coroutines.launch
import java.util.*

class NoteListViewModel(private val noteRepository: NoteRepository) : BaseViewModel() {

    private val allNote = noteRepository.allNotes

    private var filterKey = MutableLiveData<String>()

    private var selectedNote = MutableLiveData<NoteUIModel>()

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
            noteSelected = selectedNote.value
        )

    private fun transform(entity: NoteEntity) = NoteUIModel(
        entity.id,
        entity.title,
        entity.content,
        entity.createTime,
        filterKey.value.orEmpty(),
        entity.id == selectedNote.value?.id
    )

    private fun createNote(title: String, content: String) {
        uiScope.launch {
            noteRepository.saveNote(
                NoteEntity(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    content = content,
                    createTime = System.currentTimeMillis()
                )
            )
        }
    }

    private fun updateNote(note: NoteUIModel) {
        uiScope.launch {
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
        if (noteId != selectedNote.value?.id) {
            selectedNote.value =
                allNote.value?.firstOrNull { it.id == noteId }?.run { transform(this) }
        }
    }

    fun deleteSelectedNote() {
        _state.value = _state.value?.copy(isLoading = true)
        uiScope.launch {
            selectedNote.value?.let {
                when (val result = noteRepository.deleteNote(it.id)) {
                    is Result.Success -> {
                        _state.postValue(combineData())
                    }
                    is Result.Error -> {
                        _state.value =
                            _state.value?.copy(isLoading = false, error = result.exception)
                    }
                }
            }
        }
    }

    fun saveNote(title: String, content: String) {
        selectedNote.value?.let {
            updateNote(it.apply {
                this.title = title
                this.content = content
            })
        } ?: createNote(title, content)
    }
}