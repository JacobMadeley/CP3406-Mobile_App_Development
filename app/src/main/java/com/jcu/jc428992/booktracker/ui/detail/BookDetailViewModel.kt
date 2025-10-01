package com.jcu.jc428992.booktracker.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcu.jc428992.booktracker.data.local.Book
import com.jcu.jc428992.booktracker.data.local.Bookshelf
import com.jcu.jc428992.booktracker.data.local.ReadingStatus
import com.jcu.jc428992.booktracker.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookDetailUiState(
    val book: Book? = null,
    val isLoading: Boolean = true,
    val bookshelves: List<Bookshelf> = emptyList()
)

sealed class BookDetailEvent {
    object NavigateBack : BookDetailEvent()
}

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookId: Long = checkNotNull(savedStateHandle["bookId"])

    private val _uiState = MutableStateFlow(BookDetailUiState())
    val uiState: StateFlow<BookDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<BookDetailEvent>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            bookRepository.getBookById(bookId).collect { book ->
                _uiState.update { it.copy(book = book, isLoading = false) }
            }
        }
        viewModelScope.launch {
            bookRepository.getAllBookshelves().collect { bookshelves ->
                _uiState.update { it.copy(bookshelves = bookshelves) }
            }
        }
    }

    fun updateReadingStatus(newStatus: ReadingStatus) {
        _uiState.value.book?.let { currentBook ->
            viewModelScope.launch {
                bookRepository.updateBook(currentBook.copy(readingStatus = newStatus))
            }
        }
    }

    fun updateRating(newRating: Float) {
        _uiState.value.book?.let { currentBook ->
            viewModelScope.launch {
                bookRepository.updateBook(currentBook.copy(personalRating = newRating))
            }
        }
    }

    fun updateReview(newReview: String) {
        _uiState.value.book?.let { currentBook ->
            viewModelScope.launch {
                bookRepository.updateBook(currentBook.copy(personalReview = newReview))
            }
        }
    }

    fun addBookToBookshelf(bookshelfId: Long) {
        _uiState.value.book?.let { book ->
            viewModelScope.launch {
                bookRepository.addBookToBookshelf(bookId = book.id, bookshelfId = bookshelfId)
            }
        }
    }

    fun onDeleteBook() {
        _uiState.value.book?.let { bookToDelete ->
            viewModelScope.launch {
                bookRepository.deleteBook(bookToDelete)
                _events.emit(BookDetailEvent.NavigateBack)
            }
        }
    }
}
