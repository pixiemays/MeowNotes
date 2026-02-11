package com.pixiemays.meownotes

import android.app.Application
import com.pixiemays.meownotes.Notes.NotesRepository
import com.pixiemays.meownotes.Database.MeowNotesDatabase
import com.pixiemays.meownotes.Preferences.UserPreferencesRepository
import com.pixiemays.meownotes.data.TasksRepository

class MeowNotesApplication : Application() {

    private val database by lazy { MeowNotesDatabase.getDatabase(this) }

    val notesRepository by lazy { NotesRepository(database.noteDao()) }
    val tasksRepository by lazy { TasksRepository(database.taskDao()) }
    val userPreferencesRepository by lazy { UserPreferencesRepository(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MeowNotesApplication
            private set
    }
}