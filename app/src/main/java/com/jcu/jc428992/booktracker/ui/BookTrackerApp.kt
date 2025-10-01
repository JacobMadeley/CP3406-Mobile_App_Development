package com.jcu.jc428992.booktracker.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jcu.jc428992.booktracker.ui.account.AccountScreen
import com.jcu.jc428992.booktracker.ui.account.AccountViewModel
import com.jcu.jc428992.booktracker.ui.account.StatsScreen
import com.jcu.jc428992.booktracker.ui.bookshelves.BookshelfDetailScreen
import com.jcu.jc428992.booktracker.ui.bookshelves.BookshelvesScreen
import com.jcu.jc428992.booktracker.ui.common.AppDrawerContent
import com.jcu.jc428992.booktracker.ui.detail.BookDetailScreen
import com.jcu.jc428992.booktracker.ui.home.HomeScreen
import com.jcu.jc428992.booktracker.ui.icons.AppIcons
import com.jcu.jc428992.booktracker.ui.navigation.Screen
import com.jcu.jc428992.booktracker.ui.search.SearchScreen
import com.jcu.jc428992.booktracker.ui.theme.BookTrackerTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookTrackerApp(
    accountViewModel: AccountViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val appTheme by accountViewModel.appTheme.collectAsStateWithLifecycle()

    val topBarTitle = when (currentRoute) {
        Screen.Home.route -> "Home"
        Screen.Bookshelf.route -> "Bookshelves"
        Screen.Search.route -> "Search"
        Screen.Account.route -> "Account"
        else -> "Book Tracker"
    }

    BookTrackerTheme(appTheme = appTheme) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppDrawerContent(
                    navController = navController,
                    drawerState = drawerState,
                    scope = scope
                )
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(topBarTitle) },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(painter = AppIcons.MenuIcon, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            IconButton(onClick = { navController.navigate(Screen.Search.route) }) {
                                Icon(painter = AppIcons.SearchIcon, contentDescription = "Search")
                            }
                        }
                    )
                },
                bottomBar = {
                    AppNavigationBar(navController = navController)
                }
            ) { innerPadding ->
                AppNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun AppNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(Screen.Home, Screen.Bookshelf, Screen.Search, Screen.Account)

    NavigationBar {
        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    when (screen) {
                        Screen.Home -> Icon(painter = AppIcons.HomeIcon, contentDescription = "Home")
                        Screen.Bookshelf -> Icon(painter = AppIcons.Book, contentDescription = "Bookshelf")
                        Screen.Search -> Icon(painter = AppIcons.SearchIcon, contentDescription = "Search")
                        Screen.Account -> Icon(painter = AppIcons.AccountIcon, contentDescription = "Account")
                        else -> {}
                    }
                },
                label = { Text(screen.route.replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Bookshelf.route) {
            BookshelvesScreen(navController = navController)
        }
        composable(Screen.Search.route) {
            SearchScreen()
        }
        composable(Screen.Account.route) { // Now there is only one
            AccountScreen(navController = navController)
        }
        composable(Screen.Stats.route) {
            StatsScreen()
        }
        composable(
            route = Screen.BookshelfDetail.route,
            arguments = listOf(navArgument("bookshelfId") { type = NavType.LongType })
        ) {
            BookshelfDetailScreen(navController = navController)
        }
        composable(
            route = Screen.BookDetail.route,
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) {
            BookDetailScreen(navController = navController)
        }
    }
}

