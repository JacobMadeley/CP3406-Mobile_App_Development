package com.jcu.jc428992.booktracker.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.jcu.jc428992.booktracker.data.local.Book
import com.jcu.jc428992.booktracker.data.local.Bookshelf
import com.jcu.jc428992.booktracker.ui.icons.AppIcons
import com.jcu.jc428992.booktracker.ui.navigation.Screen

@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CurrentlyReadingSection(book = uiState.currentlyReading)

            BookshelfCarousel(
                bookshelves = uiState.bookshelves,
                onBookshelfClick = { bookshelfId ->
                    navController.navigate(Screen.BookshelfDetail.createRoute(bookshelfId))
                }
            )

            RecommendationCarousel(
                recommendations = uiState.recommendations,
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetail.createRoute(bookId))
                }
            )
        }
    }
}

    @Composable
    fun CurrentlyReadingSection(book: Book?) {
        Column {
            Text(
                text = "Currently Reading",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (book != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = book.coverUrl,
                            contentDescription = "Cover for ${book.title}",
                            modifier = Modifier.size(60.dp, 90.dp),
                            placeholder = AppIcons.Book,
                            error = AppIcons.Book
                        )
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(text = book.title, style = MaterialTheme.typography.titleMedium)
                            Text(text = book.author, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            } else {
                Text(
                    text = "No book currently reading",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    @Composable
    private fun BookshelfCarousel(
        bookshelves: List<Bookshelf>,
        onBookshelfClick: (Long) -> Unit
    ) {
        Column {
            Text(
                text = "Your Bookshelves",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(bookshelves) { bookshelf ->
                    Card(
                        modifier = Modifier
                            .size(150.dp, 100.dp)
                            .clickable { onBookshelfClick(bookshelf.id) }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = bookshelf.name,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }

@Composable
private fun RecommendationCarousel(
    recommendations: List<Book>,
    onBookClick: (Long) -> Unit
) {
    Column {
        Text(
            text = "Recommendations",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(recommendations) { book ->
                Card(
                    modifier = Modifier
                        .width(120.dp)
                        .clickable { onBookClick(book.id) } // Make the whole card clickable
                ) {
                    Column {
                        AsyncImage(
                            model = book.coverUrl,
                            contentDescription = "Cover for ${book.title}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(2f / 3f),
                            placeholder = AppIcons.Book,
                            error = AppIcons.Book
                        )
                        Column(Modifier.padding(8.dp)) {
                            Text(
                                text = book.title,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = book.author,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
