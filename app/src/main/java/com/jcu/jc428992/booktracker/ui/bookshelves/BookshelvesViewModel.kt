package com.jcu.jc428992.booktracker.ui.bookshelves

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcu.jc428992.booktracker.data.local.Bookshelf
import com.jcu.jc428992.booktracker.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookshelvesUiState(
    val bookshelves: List<Bookshelf> = emptyList()
)

@HiltViewModel
class BookshelvesViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    val uiState: StateFlow<BookshelvesUiState> =
        bookRepository.getAllBookshelves()
            .map { BookshelvesUiState(bookshelves = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = BookshelvesUiState()
            )

    fun createBookshelf(name: String) {
        // Don't create bookshelves with blank names
        if (name.isNotBlank()) {
            viewModelScope.launch {
                bookRepository.createBookshelf(name)
            }
        }
    }

    fun deleteBookshelf(bookshelf: Bookshelf) {
        viewModelScope.launch {
            bookRepository.deleteBookshelf(bookshelf)
        }
    }
}
