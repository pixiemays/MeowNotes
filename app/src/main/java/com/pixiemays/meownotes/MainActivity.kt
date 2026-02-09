package com.pixiemays.meownotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.pixiemays.meownotes.data.Note
import com.pixiemays.meownotes.ui.screens.*
import com.pixiemays.meownotes.ui.theme.AppTheme
import com.pixiemays.meownotes.ui.theme.MeowNotesTheme
import com.pixiemays.meownotes.viewmodel.NotesViewModel
import com.pixiemays.meownotes.viewmodel.TasksViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            var currentTheme by remember { mutableStateOf(AppTheme.PURPLE) }
            var isDarkMode by remember { mutableStateOf(false) }

            MeowNotesTheme(
                darkTheme = isDarkMode,
                appTheme = currentTheme
            ) {
                MeowNotesApp(
                    currentTheme = currentTheme,
                    isDarkMode = isDarkMode,
                    onThemeChange = { currentTheme = it },
                    onDarkModeToggle = { isDarkMode = it }
                )
            }
        }
    }
}

@Preview
@Composable
fun MewoNotesPreview() {
    var currentTheme by remember { mutableStateOf(AppTheme.PURPLE) }
    var isDarkMode by remember { mutableStateOf(false) }

    MeowNotesApp(
        currentTheme = currentTheme,
        isDarkMode = isDarkMode,
        onThemeChange = { currentTheme = it },
        onDarkModeToggle = { isDarkMode = it }
    )
}

@Composable
fun MeowNotesApp(
    currentTheme: AppTheme,
    isDarkMode: Boolean,
    onThemeChange: (AppTheme) -> Unit,
    onDarkModeToggle: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val notesViewModel: NotesViewModel = viewModel()
    val tasksViewModel: TasksViewModel = viewModel()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Показывать нижнюю навигацию только на главных экранах
    val showBottomBar = currentRoute in listOf(
        NavRoutes.Notes.route,
        NavRoutes.Tasks.route,
        NavRoutes.Settings.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Notes.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavRoutes.Notes.route) {
                NotesScreen(
                    viewModel = notesViewModel,
                    onNoteClick = { note ->
                        navController.navigate("${NavRoutes.EditNote.route}/${note.id}")
                    },
                    onAddClick = {
                        navController.navigate(NavRoutes.AddNote.route)
                    }
                )
            }

            composable(NavRoutes.Tasks.route) {
                TasksScreen(
                    viewModel = tasksViewModel,
                    onTaskClick = { task ->
                        navController.navigate("${NavRoutes.EditTask.route}/${task.id}")
                    },
                    onAddClick = {
                        navController.navigate(NavRoutes.AddTask.route)
                    }
                )
            }

            composable(NavRoutes.Settings.route) {
                SettingsScreen(
                    currentTheme = currentTheme,
                    isDarkMode = isDarkMode,
                    onThemeChange = onThemeChange,
                    onDarkModeToggle = onDarkModeToggle
                )
            }

            composable(NavRoutes.AddNote.route) {
                AddEditNoteScreen(
                    onSave = { note ->
                        notesViewModel.addNote(note)
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = "${NavRoutes.EditNote.route}/{noteId}",
                arguments = listOf(navArgument("noteId") { type = NavType.StringType })
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getString("noteId")
                val notes by notesViewModel.filteredNotes.collectAsState()
                val note = notes.find { it.id == noteId }

                if (note != null) {
                    AddEditNoteScreen(
                        note = note,
                        onSave = { updatedNote ->
                            notesViewModel.updateNote(updatedNote)
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }

            composable(NavRoutes.AddTask.route) {
                AddEditTaskScreen(
                    onSave = { task ->
                        tasksViewModel.addTask(task)
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = "${NavRoutes.EditTask.route}/{taskId}",
                arguments = listOf(navArgument("taskId") { type = NavType.StringType })
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId")
                val tasks by tasksViewModel.filteredTasks.collectAsState()
                val task = tasks.find { it.id == taskId }

                if (task != null) {
                    AddEditTaskScreen(
                        task = task,
                        onSave = { updatedTask ->
                            tasksViewModel.updateTask(updatedTask)
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primaryContainer,

    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        NavBarItems.BarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.label
                    )
                },
                label = {
                    Text(text = navItem.label)
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    }
}

data class BarItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            label = "Заметки",
            icon = Icons.Default.Create,
            route = "notes"
        ),
        BarItem(
            label = "Задачи",
            icon = Icons.Default.CheckCircle,
            route = "tasks"
        ),
        BarItem(
            label = "Настройки",
            icon = Icons.Default.Settings,
            route = "settings"
        )
    )
}

sealed class NavRoutes(val route: String) {
    object Notes : NavRoutes("notes")
    object Tasks : NavRoutes("tasks")
    object Settings : NavRoutes("settings")
    object AddNote : NavRoutes("add_note")
    object EditNote : NavRoutes("edit_note")
    object AddTask : NavRoutes("add_task")
    object EditTask : NavRoutes("edit_task")
}