package com.pixiemays.meownotes

import android.app.Fragment
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pixiemays.meownotes.ui.theme.MeowNotesTheme

private val selectedFragment: String = "";

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeowNotesTheme {
                MeowNotesApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun MeowNotesApp() {
    val navController = rememberNavController()
    Column(Modifier.padding(8.dp)) {
        NavHost(navController, startDestination = NavRoutes.Notes.route, modifier = Modifier.weight(1f)) {
            composable(NavRoutes.Notes.route) { Notes() }
            composable(NavRoutes.Tasks.route) { Tasks()  }
            composable(NavRoutes.Settings.route) { Settings() }
        }
        BottomNavigationBar(navController = navController)
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        NavBarItems.BarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {saveState = true}
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(imageVector = navItem.icon,
                        contentDescription = navItem.label)
                },
                label = {
                    Text(text = navItem.label)
                }
            )
        }
    }
}

@Composable
fun Notes() {
    Text("Notes", fontSize = 30.sp)
}

@Composable
fun Tasks() {
    Text("Tasks", fontSize = 30.sp)
}

@Composable
fun Settings() {
    Text("Settings", fontSize = 30.sp)
}

data class BarItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            label = "Notes",
            icon = Icons.Default.Create,
            route = "notes"
        ),
        BarItem(
            label = "Tasks",
            icon = Icons.Default.CheckCircle,
            route = "tasks"
        ),
        BarItem(
            label = "About",
            icon = Icons.Default.Settings,
            route = "settings"
        )
    )
}

sealed class NavRoutes(val route: String) {
    object Notes : NavRoutes("notes")
    object Tasks : NavRoutes("tasks")
    object Settings : NavRoutes("settings")
}