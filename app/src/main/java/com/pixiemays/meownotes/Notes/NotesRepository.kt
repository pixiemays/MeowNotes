package com.pixiemays.meownotes.data

import com.pixiemays.meownotes.SortType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotesRepository {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    fun addNote(note: Note) {
        _notes.value = _notes.value + note
    }

    fun updateNote(note: Note) {
        _notes.value = _notes.value.map {
            if (it.id == note.id) note else it
        }
    }

    fun deleteNote(noteId: String) {
        _notes.value = _notes.value.filter { it.id != noteId }
    }

    fun toggleNoteCompletion(noteId: String) {
        _notes.value = _notes.value.map {
            if (it.id == noteId) {
                it.copy(
                    modifiedDate = System.currentTimeMillis()
                )
            } else it
        }
    }

    fun getFilteredAndSortedNotes(
        category: NoteCategory?,
        sortType: SortType
    ): List<Note> {
        var filtered = _notes.value

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