package com.example.signalstrengthlab.ui

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.signalstrengthlab.R
import com.example.signalstrengthlab.app.theme.SignalStrengthTheme
import com.example.signalstrengthlab.ui.mapping.MappingScreen
import com.example.signalstrengthlab.ui.navigation.SignalStrengthTabs
import com.example.signalstrengthlab.ui.users.UsersScreen
import com.example.signalstrengthlab.utils.ui.ImeAvoidingBox
import com.example.signalstrengthlab.utils.ui.Keyboard
import com.example.signalstrengthlab.utils.ui.keyboardAsState
import java.util.*

@Composable
fun SignalStrengthApp() {
    SignalStrengthTheme {
        MainScreen()
    }
}

@Composable
fun MainScreen() {
    val keyboardState by keyboardAsState()
    if (keyboardState == Keyboard.Closed)
        LocalFocusManager.current.clearFocus()

    val tabs = remember { SignalStrengthTabs.values() }
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = { TopBar() },
        bottomBar = { BottomNavigationBar(navController, tabs) }
    ) {
        ImeAvoidingBox(bottomPadding = it.calculateBottomPadding()) {
            Navigation(navController = navController)
        }
    }
}

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController,
        startDestination = SignalStrengthTabs.MAPPING.route,
        modifier = modifier
    ) {
        composable(SignalStrengthTabs.MAPPING.route) {
            MappingScreen()
        }
        composable(SignalStrengthTabs.USERS.route) {
            UsersScreen()
        }
    }
}

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = stringResource(R.string.app_name)) },
    )
}

@Composable
fun BottomNavigationBar(navController: NavController, tabs: Array<SignalStrengthTabs>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
        ?: SignalStrengthTabs.MAPPING.route

    val routes = remember { SignalStrengthTabs.values().map { it.route } }
    if (currentRoute in routes) {
        BottomNavigation {
            tabs.forEach { tab ->
                BottomNavigationItem(
                    icon = { Icon(imageVector = tab.icon, contentDescription = null) },
                    label = { Text(text = stringResource(tab.title).uppercase(Locale.getDefault())) },
                    selected = currentRoute == tab.route,
                    onClick = {
                        if (tab.route != currentRoute) {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    alwaysShowLabel = false,
                    selectedContentColor = MaterialTheme.colors.primaryVariant,
                    unselectedContentColor = LocalContentColor.current,
                )
            }
        }
    }
}