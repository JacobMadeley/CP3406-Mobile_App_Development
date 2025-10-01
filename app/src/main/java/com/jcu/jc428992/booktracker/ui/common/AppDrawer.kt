package com.jcu.jc428992.booktracker.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jcu.jc428992.booktracker.ui.icons.AppIcons
import com.jcu.jc428992.booktracker.ui.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppDrawerContent(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    ModalDrawerSheet {
        Spacer(Modifier.height(12.dp))

        NavigationDrawerItem(
            icon = { Icon(AppIcons.HomeIcon, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false, // We can add logic to highlight the current screen later
            onClick = {
                navController.navigate(Screen.Home.route)
                scope.launch { drawerState.close() } // Close the drawer after navigating
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            icon = { Icon(painter = AppIcons.Book, contentDescription = "Bookshelves") },
            label = { Text("My Bookshelves") },
            selected = false,
            onClick = {
                navController.navigate(Screen.Bookshelf.route)
                scope.launch { drawerState.close() }
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            icon = { Icon(AppIcons.AccountIcon, contentDescription = "Account") },
            label = { Text("Account") },
            selected = false,
            onClick = {
                navController.navigate(Screen.Account.route)
                scope.launch { drawerState.close() }
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}
