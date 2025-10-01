package com.jcu.jc428992.booktracker.ui.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.jcu.jc428992.booktracker.data.repository.AppTheme
import com.jcu.jc428992.booktracker.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavHostController,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val currentTheme by viewModel.appTheme.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showThemeDialog by remember { mutableStateOf(false) }

    if (showThemeDialog) {
        ThemePickerDialog(
            currentTheme = currentTheme,
            onThemeSelected = {
                viewModel.setTheme(it)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }

    if (uiState.isLogoutDialogVisible) {
        LogOutConfirmationDialog(
            onConfirm = viewModel::onConfirmLogOut,
            onDismiss = viewModel::onDismissLogOutDialog
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(onClick = { showThemeDialog = true }) {
            ListItem(
                headlineContent = { Text("App Theme") },
                supportingContent = { Text(currentTheme.name) }
            )
        }
        Card(onClick = { navController.navigate(Screen.Stats.route) }) {
            ListItem(
                headlineContent = { Text("Reading Stats") },
                supportingContent = { Text("View your reading history and statistics") }
            )
        }
        Card(onClick = viewModel::onLogOutClicked) {
            ListItem(
                headlineContent = { Text("Log Out") }
            )
        }
    }
}

@Composable
private fun ThemePickerDialog(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose Theme") },
        text = {
            Column {
                AppTheme.entries.forEach { theme ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onThemeSelected(theme) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentTheme == theme,
                            onClick = { onThemeSelected(theme) }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(theme.name)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun LogOutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Out") },
        text = { Text("Are you sure you want to log out?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
