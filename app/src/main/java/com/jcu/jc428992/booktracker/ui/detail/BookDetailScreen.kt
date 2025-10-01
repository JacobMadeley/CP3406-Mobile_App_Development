package com.jcu.jc428992.booktracker.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jcu.jc428992.booktracker.data.local.Book
import com.jcu.jc428992.booktracker.data.local.Bookshelf
import com.jcu.jc428992.booktracker.data.local.ReadingStatus
import com.jcu.jc428992.booktracker.ui.icons.AppIcons
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    navController: NavController,
    viewModel: BookDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is BookDetailEvent.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(uiState.book?.title ?: "Book Details") },
                actions = {
                    IconButton(onClick = viewModel::onDeleteBook) {
                        Icon(
                            AppIcons.DeleteIcon,
                            contentDescription = "Delete Book"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                uiState.book?.let { book ->
                    BookDetailsContent(
                        book = book,
                        bookshelves = uiState.bookshelves,
                        onStatusChange = viewModel::updateReadingStatus,
                        onRatingChange = viewModel::updateRating,
                        onReviewChange = viewModel::updateReview,
                        onAddToBookshelf = viewModel::addBookToBookshelf
                    )
                }
            }
        }
    }
}

@Composable
private fun BookDetailsContent(
    book: Book,
    bookshelves: List<Bookshelf>,
    onStatusChange: (ReadingStatus) -> Unit,
    onRatingChange: (Float) -> Unit,
    onReviewChange: (String) -> Unit,
    onAddToBookshelf: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Book Cover Image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(book.coverUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Cover for ${book.title}",
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth(0.6f),
            placeholder = AppIcons.Book,
            error = AppIcons.Book
        )
        // Title and Author
        Text(text = book.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(text = book.author, style = MaterialTheme.typography.titleMedium)
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        StatusSelector(
            currentStatus = book.readingStatus,
            onStatusChange = onStatusChange
        )
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        AddToBookShelfMenu(
            bookshelves = bookshelves,
            onAddToBookshelf = onAddToBookshelf
        )
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        RatingBar(
            currentRating = book.personalRating ?: 0f,
            onRatingChange = onRatingChange
        )
        OutlinedTextField(
            value = book.personalReview ?: "",
            onValueChange = { onReviewChange(it) },
            label = { Text("Write a review") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
    }
}

@Composable
private fun StatusSelector(
    currentStatus: ReadingStatus,
    onStatusChange: (ReadingStatus) -> Unit
) {
    val statuses = listOf(ReadingStatus.READING, ReadingStatus.READ, ReadingStatus.ABANDONED)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Update Status", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            statuses.forEach { status ->
                Button(
                    onClick = { onStatusChange(status) },
                    colors = if (currentStatus == status) {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        ButtonDefaults.buttonColors()
                    }
                ) {
                    Text(text = status.name.replace("_", " ").lowercase().replaceFirstChar { it.titlecase() })
                }
            }
        }
    }
}

@Composable
private fun RatingBar(
    currentRating: Float,
    onRatingChange: (Float) -> Unit,
    stars: Int = 5
) {
    Row {
        for (i in 1..stars) {
            Icon(
                painter = if (i <= currentRating) AppIcons.StarIconFilled else AppIcons.StarIcon,
                contentDescription = "Star $i",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { onRatingChange(i.toFloat()) },
                tint = if (i <= currentRating) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AddToBookShelfMenu(
    bookshelves: List<Bookshelf>,
    onAddToBookshelf: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Icon(AppIcons.LibraryAdd, contentDescription = "Add to bookshelf")
            Spacer(Modifier.width(8.dp))
            Text("Add to Bookshelf")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            bookshelves.forEach { bookshelf ->
                DropdownMenuItem(
                    text = { Text(bookshelf.name) },
                    onClick = {
                        onAddToBookshelf(bookshelf.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
