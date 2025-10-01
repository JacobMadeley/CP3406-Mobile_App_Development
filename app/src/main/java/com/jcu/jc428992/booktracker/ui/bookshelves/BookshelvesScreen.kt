package com.jcu.jc428992.booktracker.ui.bookshelves

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.jcu.jc428992.booktracker.ui.icons.AppIcons
import com.jcu.jc428992.booktracker.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelvesScreen(
    navController: NavHostController,
    viewModel: BookshelvesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var newBookshelfName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Create a New Bookshelf", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = newBookshelfName,
                onValueChange = { newBookshelfName = it },
                label = { Text("Bookshelf Name") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                viewModel.createBookshelf(newBookshelfName)
                newBookshelfName = "" // Clear the text field
            }) {
                Text("Create")
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
        Text(text = "Your Bookshelves", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = uiState.bookshelves,
                key = { it.id }
            ) { bookshelf ->
                val dismissState = rememberSwipeToDismissBoxState()
                if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                    LaunchedEffect(bookshelf) {
                        viewModel.deleteBookshelf(bookshelf)
                    }
                }
                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = {
                        val color = when (dismissState.dismissDirection) {
                            SwipeToDismissBoxValue.EndToStart -> Color.Red
                            else -> Color.Transparent
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color, shape = CardDefaults.shape)
                                .padding(16.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                painter = AppIcons.DeleteIcon,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                        }
                    }
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            navController.navigate(Screen.BookshelfDetail.createRoute(bookshelf.id))
                        }
                    ) {
                        Text(
                            text = bookshelf.name,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
