package com.haq.mynote.ui.notelist

data class NotesState(
    val isLoading: Boolean = false,
    val noteList: List<NoteUIModel> = emptyList(),
    val noteSelected: NoteUIModel? = null,
    val error: Throwable? = null
)