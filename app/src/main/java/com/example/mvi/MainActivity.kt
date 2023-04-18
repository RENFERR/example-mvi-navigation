package com.example.mvi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mvi.destinations.ListScreenDestination
import com.example.mvi.destinations.LoginScreenDestination
import com.example.mvi.destinations.UserScreenDestination
import com.example.mvi.screens.drawer.contract.DrawerViewModel
import com.example.mvi.screens.drawer.view.Drawer
import com.example.mvi.screens.list.view.ListScreen
import com.example.mvi.screens.login.viewmodel.IsLoggedViewModel
import com.example.mvi.screens.user.view.UserScreen
import com.example.mvi.ui.theme.MVIExampleTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val isLoggedViewModel: IsLoggedViewModel = hiltViewModel()
            val drawerViewModel: DrawerViewModel = hiltViewModel()
            val startRoute = if (!isLoggedViewModel.isLogged) LoginScreenDestination
            else NavGraphs.root.startRoute
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

            MVIExampleTheme {
                DestinationsNavHost(
                    startRoute = startRoute,
                    navGraph = NavGraphs.root,
                    navController = navController
                ) {
                    composable(ListScreenDestination) {
                        Drawer(
                            state = drawerState,
                            viewModel = drawerViewModel,
                            navController = navController
                        ) {
                            ListScreen(
                                navigator = destinationsNavigator,
                                drawerState = drawerState
                            )
                        }
                    }
                    composable(UserScreenDestination) {
                        Drawer(
                            state = drawerState,
                            viewModel = drawerViewModel,
                            navController = navController
                        ) {
                            UserScreen(
                                navigator = destinationsNavigator,
                                drawerState = drawerState
                            )
                        }
                    }
                }
                ShowLoginWhenLoggedOut(
                    viewModel = isLoggedViewModel,
                    navController = navController
                )
            }
        }
    }

    @Composable
    private fun ShowLoginWhenLoggedOut(
        viewModel: IsLoggedViewModel,
        navController: NavHostController
    ) {
        val currentDestination by navController.appCurrentDestinationAsState()
        val isLoggedIn by viewModel.isLoggedFlow.collectAsState()
        val isNeedGoToLogin = !isLoggedIn
                && currentDestination != null
                && currentDestination != LoginScreenDestination

        if (isNeedGoToLogin) {
            navController.navigate(LoginScreenDestination) {
                launchSingleTop = true
            }
        }
    }
}