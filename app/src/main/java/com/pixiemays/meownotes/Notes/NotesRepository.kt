package com.pixiemays.meownotes.Notes

import com.pixiemays.meownotes.SortType
import com.pixiemays.meownotes.Database.NoteDao
import com.pixiemays.meownotes.Database.toEntity
import com.pixiemays.meownotes.Database.toNote
import com.pixiemays.meownotes.data.Note
import com.pixiemays.meownotes.data.NoteCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepository(private val noteDao: NoteDao) {

    val notes: Flow<List<Note>> = noteDao.getAllNotes().map { entities ->
        entities.map { it.toNote() }
    }

    suspend fun addNote(note: Note) {
        noteDao.insertNote(note.toEntity())
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.toEntity())
    }

    suspend fun deleteNote(noteId: String) {
        noteDao.deleteNoteById(noteId)
    }

    suspend fun getNoteById(noteId: String): Note? {
        return noteDao.getNoteById(noteId)?.toNote()
    }

    fun getFilteredAndSortedNotes(
        notesList: List<Note>,
        category: NoteCategory?,
        sortType: SortType
    ): List<Note> {
        var filtered = notesList

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