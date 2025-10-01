package com.jcu.jc428992.booktracker.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcu.jc428992.booktracker.data.local.Book
import com.jcu.jc428992.booktracker.data.repository.BookRepository
import com.jcu.jc428992.booktracker.data.repository.SearchType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchType: SearchType = SearchType.TITLE
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    private var searchJob: Job? = null

    fun onSearchTypeChange(newSearchType: SearchType) {
        _uiState.update { it.copy(searchType = newSearchType) }
        onSearchQueryChange(_uiState.value.searchQuery)
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }

        searchJob?.cancel()

        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList(), isLoading = false) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(500L)
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            bookRepository.searchBooksOnline(query, _uiState.value.searchType)
                .onSuccess { books ->
                    _uiState.update {
                        it.copy(searchResults = books, isLoading = false)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(errorMessage = "Failed to fetch books.", isLoading = false)
                    }
                }
        }
    }

    fun onBookSelected(book: Book) {
        viewModelScope.launch {
            bookRepository.addBook(book)
            _events.emit("'${book.title}' added to your library!")
        }
    }
}
