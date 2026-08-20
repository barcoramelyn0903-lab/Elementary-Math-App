package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.navigation.Screen
import com.example.ui.screens.*
import com.example.ui.theme.MathQuestTheme
import com.example.ui.viewmodel.MathQuestViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MathQuestApp()
        }
    }
}

@Composable
fun MathQuestApp(viewModel: MathQuestViewModel = viewModel()) {
    val userProfile by viewModel.userProfile.collectAsState()
    val isSpaceTheme = userProfile.currentThemeId == "space"

    MathQuestTheme(darkTheme = isSpaceTheme) {
        Surface(modifier = Modifier.fillMaxSize()) {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToWorldMap = {
                            navController.navigate(Screen.WorldMap.route)
                        },
                        onNavigateToGame = {
                            navController.navigate(Screen.Game.route)
                        },
                        onNavigateToShop = {
                            navController.navigate(Screen.AvatarShop.route)
                        },
                        onNavigateToParentDashboard = {
                            navController.navigate(Screen.ParentDashboard.route)
                        },
                        onNavigateToProfile = {
                            navController.navigate(Screen.ParentDashboard.route)
                        }
                    )
                }

                composable(Screen.WorldMap.route) {
                    WorldMapScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onStartLevel = {
                            navController.navigate(Screen.Game.route)
                        }
                    )
                }

                composable(Screen.Game.route) {
                    GameScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onNavigateToMap = {
                            navController.popBackStack(Screen.WorldMap.route, false)
                        }
                    )
                }

                composable(Screen.AvatarShop.route) {
                    AvatarShopScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.ParentDashboard.route) {
                    ParentDashboardScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
