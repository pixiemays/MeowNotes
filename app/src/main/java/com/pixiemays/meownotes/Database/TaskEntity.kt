package com.pixiemays.meownotes.Database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pixiemays.meownotes.data.Task
import com.pixiemays.meownotes.data.TaskCategory

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val category: String,
    val createdDate: Long,
    val modifiedDate: Long,
    val isCompleted: Boolean
)

fun TaskEntity.toTask(): Task {
    return Task(
        id = id,
        title = title,
        content = content,
        category = TaskCategory.valueOf(category),
        createdDate = createdDate,
        modifiedDate = modifiedDate,
        isCompleted = isCompleted
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        content = content,
        category = category.name,
        createdDate = createdDate,
        modifiedDate = modifiedDate,
        isCompleted = isCompleted
    )
}