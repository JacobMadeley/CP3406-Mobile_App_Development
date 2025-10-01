package com.jcu.jc428992.booktracker.ui.bookshelves

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jcu.jc428992.booktracker.data.local.Book
import com.jcu.jc428992.booktracker.ui.common.BookGridItem
import com.jcu.jc428992.booktracker.ui.icons.AppIcons
import com.jcu.jc428992.booktracker.ui.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfDetailScreen(
    navController: NavController,
    viewModel: BookshelfDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (uiState.isAddBookDialogOpen) {
        AddBookToBookshelfDialog(
            allBooks = uiState.allBooks,
            onDismiss = viewModel::onDismissDialog,
            onBookSelected = viewModel::onBookSelected
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.bookshelfWithBooks?.bookshelf?.name ?: "Bookshelf") },
                actions = {
                    IconButton(onClick = viewModel::onAddBookClicked) {
                        Icon(AppIcons.LibraryAdd, contentDescription = "Add Book")
                    }
                }
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            uiState.bookshelfWithBooks?.let { bookshelfWithBooks ->
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 120.dp),
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(bookshelfWithBooks.books) { book ->
                        BookGridItem(
                            book = book,
                            onClick = {
                                navController.navigate(Screen.BookDetail.createRoute(book.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddBookToBookshelfDialog(
    allBooks: List<Book>,
    onDismiss: () -> Unit,
    onBookSelected: (Long) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add this book to this bookshelf") },
        text = {
            LazyColumn{
                items(allBooks) { book ->
                    Text(
                        text = "${book.title} by ${book.author}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable{ onBookSelected(book.id) }
                            .padding(vertical = 12.dp)
                    )
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
