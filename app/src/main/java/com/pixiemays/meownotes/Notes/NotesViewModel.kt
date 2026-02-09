package com.pixiemays.meownotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixiemays.meownotes.FilterStatus
import com.pixiemays.meownotes.SortType
import com.pixiemays.meownotes.data.*
import kotlinx.coroutines.flow.*

class NotesViewModel : ViewModel() {
    private val repository = NotesRepository()

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
        repository.getFilteredAndSortedNotes(category,  sort)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addNote(note: Note) {
        repository.addNote(note)
    }

    fun updateNote(note: Note) {
        repository.updateNote(note)
    }

    fun deleteNote(noteId: String) {
        repository.deleteNote(noteId)
    }

    fun toggleNoteCompletion(noteId: String) {
        repository.toggleNoteCompletion(noteId)
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
}