package com.pixiemays.meownotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pixiemays.meownotes.FilterStatus
import com.pixiemays.meownotes.MeowNotesApplication
import com.pixiemays.meownotes.SortType
import com.pixiemays.meownotes.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TasksViewModel(
    private val repository: TasksRepository
) : ViewModel() {

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
        repository.getFilteredAndSortedTasks(tasks, category, status, sort)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.addTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }

    fun toggleTaskCompletion(taskId: String) {
        viewModelScope.launch {
            repository.toggleTaskCompletion(taskId)
        }
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

    companion object {
        fun provideFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TasksViewModel(
                    MeowNotesApplication.instance.tasksRepository
                ) as T
            }
        }
    }
}