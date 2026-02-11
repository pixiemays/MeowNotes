package com.pixiemays.meownotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pixiemays.meownotes.MeowNotesApplication
import com.pixiemays.meownotes.Preferences.UserPreferencesRepository
import com.pixiemays.meownotes.ui.theme.AppTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val userPreferences: StateFlow<com.pixiemays.meownotes.Preferences.UserPreferences?> =
        preferencesRepository.userPreferencesFlow
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null
            )

    fun updateTheme(theme: AppTheme) {
        viewModelScope.launch {
            preferencesRepository.updateTheme(theme)
        }
    }

    fun updateDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            preferencesRepository.updateDarkMode(isDarkMode)
        }
    }

    fun setFirstLaunchComplete() {
        viewModelScope.launch {
            preferencesRepository.setFirstLaunchComplete()
        }
    }

    companion object {
        fun provideFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(
                    MeowNotesApplication.instance.userPreferencesRepository
                ) as T
            }
        }
    }
}