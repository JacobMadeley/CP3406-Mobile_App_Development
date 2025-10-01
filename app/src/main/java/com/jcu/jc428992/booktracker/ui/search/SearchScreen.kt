package com.jcu.jc428992.booktracker.ui.search

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jcu.jc428992.booktracker.data.local.Book
import com.jcu.jc428992.booktracker.data.repository.SearchType
import com.jcu.jc428992.booktracker.ui.icons.AppIcons
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            PrimaryTabRow(selectedTabIndex = uiState.searchType.ordinal) {
                SearchType.entries.forEach { searchType ->
                    Tab(
                        selected = uiState.searchType == searchType,
                        onClick = { viewModel.onSearchTypeChange(searchType) },
                        text = { Text(searchType.name.replaceFirstChar { it.titlecase() }) }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                label = "Search by ${uiState.searchType.name.lowercase().replaceFirstChar { it.titlecase() }}"
            )

            Spacer(Modifier.height(16.dp))
            SearchResults(
                uiState = uiState,
                onBookSelected = viewModel::onBookSelected
            )
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = { Icon(AppIcons.SearchIcon, contentDescription = "Search Icon") },
        singleLine = true
    )
}

@Composable
private fun SearchResults(
    uiState: SearchUiState,
    onBookSelected: (Book) -> Unit
) {
    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        uiState.errorMessage != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
        uiState.searchResults.isEmpty() && uiState.searchQuery.isNotBlank() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No books found for '${uiState.searchQuery}'.")
            }
        }
        uiState.searchResults.isNotEmpty() -> {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.searchResults) { book ->
                    SearchResultItem(
                        book = book,
                        onBookSelected = { onBookSelected(book) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    book: Book,
    onBookSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onBookSelected),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Use Coil's AsyncImage to load the book cover from the URL
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(book.coverUrl)
                    .crossfade(true)
                    .build(),
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
}
