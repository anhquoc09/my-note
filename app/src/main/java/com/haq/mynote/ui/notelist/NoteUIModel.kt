package com.haq.mynote.ui.notelist

data class NoteUIModel(
    var id: String = "",
    var title: String = "",
    var content: String = "",
    var createTime: Long = 0,
    var highLightKey: String = "",
    var isSelected: Boolean = false
)