package com.pixiemays.meownotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixiemays.meownotes.FilterStatus
import com.pixiemays.meownotes.SortType
import com.pixiemays.meownotes.data.*
import com.pixiemays.meowtasks.data.TasksRepository
import kotlinx.coroutines.flow.*

class TasksViewModel : ViewModel() {
    private val repository = TasksRepository()

    private val _selectedCategory = MutableStateFlow<TaskCategory?>(null)
    private val _filterStatus = MutableStateFlow(FilterStatus.ALL)
    private val _sortType = MutableStateFlow(SortType.DATE_NEW_FIRST)

    val selectedCategory = _selectedCategory.asStateFlow()
    val filterStatus = _filterStatus.asStateFlow()
    val sortType = _sortType.asStateFlow()

    val filteredTasks = combine(
        repository.tasks,
        _selectedCategory,
        _filterStatus,
        _sortType
    ) { tasks, category, status, sort ->
        repository.getFilteredAndSortedTasks(category, status, sort)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addTask(task: Task) {
        repository.addTask(task)
    }

    fun updateTask(task: Task) {
        repository.updateTask(task)
    }

    fun deleteTask(taskId: String) {
        repository.deleteTask(taskId)
    }

    fun toggleTaskCompletion(taskId: String) {
        repository.toggleTaskCompletion(taskId)
    }

    fun setCategory(category: TaskCategory?) {
        _selectedCategory.value = category
    }

    fun setFilterStatus(status: FilterStatus) {
        _filterStatus.value = status
    }

    fun setSortType(sortType: SortType) {
        _sortType.value = sortType
    }
}