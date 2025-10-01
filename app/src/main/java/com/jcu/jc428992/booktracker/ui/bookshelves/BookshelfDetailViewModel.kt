package com.jcu.jc428992.booktracker.ui.bookshelves

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcu.jc428992.booktracker.data.local.Book
import com.jcu.jc428992.booktracker.data.local.BookshelfWithBooks
import com.jcu.jc428992.booktracker.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookshelfDetailUiState(
    val bookshelfWithBooks: BookshelfWithBooks? = null,
    val allBooks: List<Book> = emptyList(),
    val isAddBookDialogOpen: Boolean = false
)

@HiltViewModel
class BookshelfDetailViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookshelfId: Long = checkNotNull(savedStateHandle["bookshelfId"])

    // The private, mutable state that the ViewModel can change
    private val _uiState = MutableStateFlow(BookshelfDetailUiState())
    // The public, read-only state for the UI
    val uiState: StateFlow<BookshelfDetailUiState> = _uiState.asStateFlow()

    init {
        // Collect the specific bookshelf with its books
        viewModelScope.launch {
            bookRepository.getBookshelfWithBooks(bookshelfId).filterNotNull().collect { bookshelfWithBooks ->
                _uiState.update { it.copy(bookshelfWithBooks = bookshelfWithBooks) }
            }
        }
        // Collect the list of all books for the "add book" dialog
        viewModelScope.launch {
            bookRepository.getAllBooks().collect { allBooks ->
                _uiState.update { it.copy(allBooks = allBooks) }
            }
        }
    }

    fun onAddBookClicked() {
        _uiState.update { it.copy(isAddBookDialogOpen = true) }
    }

    fun onDismissDialog() {
        _uiState.update { it.copy(isAddBookDialogOpen = false) }
    }

    fun onBookSelected(bookId: Long) {
        viewModelScope.launch {
            bookRepository.addBookToBookshelf(bookId = bookId, bookshelfId = bookshelfId)
            onDismissDialog()
        }
    }
}
