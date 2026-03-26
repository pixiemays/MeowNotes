package com.pixiemays.meownotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.PratikFagadiya.smoothanimationbottombar.model.SmoothAnimationBottomBarScreens
import com.PratikFagadiya.smoothanimationbottombar.properties.BottomBarProperties
import com.PratikFagadiya.smoothanimationbottombar.ui.SmoothAnimationBottomBar
import com.pixiemays.meownotes.Notes.AddEditNoteScreen
import com.pixiemays.meownotes.ui.screens.AddEditTaskScreen
import com.pixiemays.meownotes.ui.screens.NotesScreen
import com.pixiemays.meownotes.ui.screens.SettingsScreen
import com.pixiemays.meownotes.ui.screens.TasksScreen
import com.pixiemays.meownotes.ui.theme.AppTheme
import com.pixiemays.meownotes.ui.theme.MeowNotesTheme
import com.pixiemays.meownotes.viewmodel.NotesViewModel
import com.pixiemays.meownotes.viewmodel.SettingsViewModel
import com.pixiemays.meownotes.viewmodel.TasksViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModel.provideFactory()
            )
            val userPreferences by settingsViewModel.userPreferences.collectAsState()

            val currentTheme = userPreferences?.appTheme ?: AppTheme.PURPLE
            val isDarkMode = userPreferences?.isDarkMode ?: false

            MeowNotesTheme(
                darkTheme = isDarkMode,
                appTheme = currentTheme
            ) {
                MeowNotesApp(
                    currentTheme = currentTheme,
                    isDarkMode = isDarkMode,
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }
}

@Composable
fun MeowNotesApp(
    currentTheme: AppTheme,
    isDarkMode: Boolean,
    settingsViewModel: SettingsViewModel
) {
    val navController = rememberNavController()
    val notesViewModel: NotesViewModel = viewModel(
        factory = NotesViewModel.provideFactory()
    )
    val tasksViewModel: TasksViewModel = viewModel(
        factory = TasksViewModel.provideFactory()
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        NavRoutes.Notes.route,
        NavRoutes.Tasks.route,
        NavRoutes.Settings.route
    )

    val currentIndex = rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController, currentIndex)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Notes.route,
            modifier = Modifier.padding(paddingValues),
            enterTransition = {
                fadeIn(tween(durationMillis = 300, easing = LinearOutSlowInEasing))
            },
            exitTransition = {
                fadeOut(tween(durationMillis = 200, easing = FastOutLinearInEasing))
            },
            popEnterTransition = {
                fadeIn(tween(durationMillis = 300, easing = LinearOutSlowInEasing))
            },
            popExitTransition = {
                fadeOut(tween(durationMillis = 200, easing = FastOutLinearInEasing))
            }
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
                    viewModel = settingsViewModel
                )
            }

            composable(NavRoutes.AddNote.route) {
                AddEditNoteScreen(
                    onSave = { note ->
                        notesViewModel.addNote(note)
                        navController.popBackStack()
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
                } else { navController.popBackStack() }
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
fun BottomNavigationBar(
    navController: NavHostController,
    currentIndex: MutableState<Int>
) {
    val items = remember {
        NavBarItems.BarItems.map { navItem ->
            SmoothAnimationBottomBarScreens(
                navItem.route,
                navItem.label,
                navItem.icon
            )
        }
    }

    SmoothAnimationBottomBar(
        navController = navController,
        bottomNavigationItems = items,
        initialIndex = currentIndex,
        bottomBarProperties = BottomBarProperties(
            backgroundColor = MaterialTheme.colorScheme.surface,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            iconTintColor = MaterialTheme.colorScheme.primaryContainer,
            iconTintActiveColor = MaterialTheme.colorScheme.onPrimaryContainer,
            textActiveColor = MaterialTheme.colorScheme.onPrimaryContainer,
            cornerRadius = 14.dp,
        ),
    ) {}
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