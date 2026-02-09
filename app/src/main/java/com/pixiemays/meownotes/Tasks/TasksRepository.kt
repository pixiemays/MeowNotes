package com.pixiemays.meowtasks.data

import com.pixiemays.meownotes.FilterStatus
import com.pixiemays.meownotes.SortType
import com.pixiemays.meownotes.data.Task
import com.pixiemays.meownotes.data.TaskCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TasksRepository {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    fun addTask(task: Task) {
        _tasks.value = _tasks.value + task
    }

    fun updateTask(task: Task) {
        _tasks.value = _tasks.value.map {
            if (it.id == task.id) task else it
        }
    }

    fun deleteTask(taskId: String) {
        _tasks.value = _tasks.value.filter { it.id != taskId }
    }

    fun toggleTaskCompletion(taskId: String) {
        _tasks.value = _tasks.value.map {
            if (it.id == taskId) {
                it.copy(
                    isCompleted = !it.isCompleted,
                    modifiedDate = System.currentTimeMillis()
                )
            } else it
        }
    }

    fun getFilteredAndSortedTasks(
        category: TaskCategory?,
        status: FilterStatus,
        sortType: SortType
    ): List<Task> {
        var filtered = _tasks.value

        // Фильтрация по категории
        if (category != null) {
            filtered = filtered.filter { it.category == category }
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