package com.pixiemays.meownotes.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import java.util.*

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val category: TaskCategory,
    val createdDate: Long = System.currentTimeMillis(),
    val modifiedDate: Long = System.currentTimeMillis(),
    val isCompleted: Boolean
)

enum class TaskCategory(val displayName: String, val icon: ImageVector) {
    DAILY("Повседневность", Icons.Default.Home),
    WORK("Работа", Icons.Default.AccountBox),
    STUDY("Учёба", Icons.Default.DateRange),
    HEALTH("Здоровье", Icons.Default.Favorite),
    SHOPPING("Покупки", Icons.Default.ShoppingCart),
    TRAVEL("Путешествия", Icons.Default.Place),
    OTHER("Другое", Icons.Default.Star)
}