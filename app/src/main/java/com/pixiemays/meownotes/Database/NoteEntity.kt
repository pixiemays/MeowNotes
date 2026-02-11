package com.pixiemays.meownotes.Database;

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pixiemays.meownotes.data.Note
import com.pixiemays.meownotes.data.NoteCategory

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val category: String,
    val createdDate: Long,
    val modifiedDate: Long
)

fun NoteEntity.toNote(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        category = NoteCategory.valueOf(category),
        createdDate = createdDate,
        modifiedDate = modifiedDate
    )
}

fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        category = category.name,
        createdDate = createdDate,
        modifiedDate = modifiedDate
    )
}