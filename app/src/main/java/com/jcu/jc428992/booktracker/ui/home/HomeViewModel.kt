package com.jcu.jc428992.booktracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcu.jc428992.booktracker.data.local.Book
import com.jcu.jc428992.booktracker.data.local.Bookshelf
import com.jcu.jc428992.booktracker.data.local.ReadingStatus
import com.jcu.jc428992.booktracker.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class HomeUiState(
    val currentlyReading: Book? = null,
    val bookshelves: List<Bookshelf> = emptyList(),
    val recommendations: List<Book> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    bookRepository: BookRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> =
        combine(
            bookRepository.getAllBooks(),
            bookRepository.getAllBookshelves()
        ) { allBooks, allBookshelves ->
            val currentlyReadingBook = allBooks.firstOrNull {
                it.readingStatus == ReadingStatus.READING
            }
            // Find books marked as "WILL_READ" for recommendations
            val recommendedBooks = allBooks.filter {
                it.readingStatus == ReadingStatus.WILL_READ
            }

            HomeUiState(
                currentlyReading = currentlyReadingBook,
                bookshelves = allBookshelves,
                recommendations = recommendedBooks, // Add the real recommendations
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeUiState(isLoading = true)
        )
}
