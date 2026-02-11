package com.pixiemays.meownotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pixiemays.meownotes.FilterStatus
import com.pixiemays.meownotes.MeowNotesApplication
import com.pixiemays.meownotes.Notes.NotesRepository
import com.pixiemays.meownotes.SortType
import com.pixiemays.meownotes.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NotesViewModel(
    private val repository: NotesRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<NoteCategory?>(null)
    private val _filterStatus = MutableStateFlow(FilterStatus.ALL)
    private val _sortType = MutableStateFlow(SortType.DATE_NEW_FIRST)

    val selectedCategory = _selectedCategory.asStateFlow()
    val filterStatus = _filterStatus.asStateFlow()
    val sortType = _sortType.asStateFlow()

    val filteredNotes = combine(
        repository.notes,
        _selectedCategory,
        _sortType
    ) { notes, category, sort ->
        repository.getFilteredAndSortedNotes(notes, category, sort)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addNote(note: Note) {
        viewModelScope.launch {
            repository.addNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
        }
    }

    fun setCategory(category: NoteCategory?) {
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
                return NotesViewModel(
                    MeowNotesApplication.instance.notesRepository
                ) as T
            }
        }
    }
}