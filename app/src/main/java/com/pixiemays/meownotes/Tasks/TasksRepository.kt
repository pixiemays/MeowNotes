package com.pixiemays.meownotes.data

import com.pixiemays.meownotes.FilterStatus
import com.pixiemays.meownotes.SortType
import com.pixiemays.meownotes.Database.TaskDao
import com.pixiemays.meownotes.Database.toEntity
import com.pixiemays.meownotes.Database.toTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TasksRepository(private val taskDao: TaskDao) {

    val tasks: Flow<List<Task>> = taskDao.getAllTasks().map { entities ->
        entities.map { it.toTask() }
    }

    suspend fun addTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    suspend fun deleteTask(taskId: String) {
        taskDao.deleteTaskById(taskId)
    }

    suspend fun getTaskById(taskId: String): Task? {
        return taskDao.getTaskById(taskId)?.toTask()
    }

    suspend fun toggleTaskCompletion(taskId: String) {
        val task = taskDao.getTaskById(taskId)
        if (task != null) {
            val updatedTask = task.copy(
                isCompleted = !task.isCompleted,
                modifiedDate = System.currentTimeMillis()
            )
            taskDao.updateTask(updatedTask)
        }
    }

    fun getFilteredAndSortedTasks(
        tasksList: List<Task>,
        category: TaskCategory?,
        status: FilterStatus,
        sortType: SortType
    ): List<Task> {
        var filtered = tasksList

        // Фильтрация по категории
        if (category != null) {
            filtered = filtered.filter { it.category == category }
        }

        // Фильтрация по статусу
        filtered = when (status) {
            FilterStatus.ALL -> filtered
            FilterStatus.ACTIVE -> filtered.filter { !it.isCompleted }
            FilterStatus.COMPLETED -> filtered.filter { it.isCompleted }
        }

        // Сортировка
        return when (sortType) {
            SortType.DATE_NEW_FIRST -> filtered.sortedByDescending { it.createdDate }
            SortType.DATE_OLD_FIRST -> filtered.sortedBy { it.createdDate }
            SortType.TITLE_AZ -> filtered.sortedBy { it.title.lowercase() }
            SortType.CATEGORY -> filtered.sortedBy { it.category.displayName }
        }
    }
}