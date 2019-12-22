package com.haq.mynote.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
        tableName = "notes",
        indices = [Index(value = ["id"], unique = true)]
)
data class NoteEntity(
        @PrimaryKey var id: String = "",
        @ColumnInfo(name = "title") var title: String = "",
        @ColumnInfo(name = "content") var content: String = "",
        @ColumnInfo(name = "create_time") var createTime: Long = 0
)