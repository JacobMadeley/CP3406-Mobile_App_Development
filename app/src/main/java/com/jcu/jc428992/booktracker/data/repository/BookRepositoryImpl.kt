package com.jcu.jc428992.booktracker.data.repository

import com.jcu.jc428992.booktracker.data.local.*
import com.jcu.jc428992.booktracker.data.remote.OpenLibraryApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

enum class SearchType {
    TITLE, AUTHOR, GENERAL
}

@Singleton
class BookRepositoryImpl @Inject constructor(
    private val bookDao: BookDao,
    private val apiService: OpenLibraryApiService,
    private val bookshelfDao: BookshelfDao
) : BookRepository {

    override fun getAllBooks(): Flow<List<Book>> = bookDao.getAllBooks()
    override fun getBookById(id: Long): Flow<Book?> = bookDao.getBookById(id)
    override suspend fun addBook(book: Book) = bookDao.insertBook(book)
    override suspend fun updateBook(book: Book) = bookDao.updateBook(book)
    override suspend fun deleteBook(book: Book) = bookDao.deleteBook(book)
    override suspend fun deleteBookshelf(bookshelf: Bookshelf) {
        bookshelfDao.deleteBookshelf(bookshelf)
    }

    override suspend fun searchBooksOnline(query: String, searchType: SearchType): Result<List<Book>> {
        return try {
            val response = when (searchType) {
                SearchType.TITLE -> apiService.searchBooksByTitle(query)
                SearchType.AUTHOR -> apiService.searchBooksByAuthor(query)
                SearchType.GENERAL -> apiService.searchBooksGeneral(query)
            }

            val mappedBooks = response.bookDocs.mapNotNull { doc ->
                if (doc.title.isNotBlank() && !doc.authorName.isNullOrEmpty()) {
                    Book(
                        title = doc.title,
                        author = doc.authorName.joinToString(", "),
                        isbn = doc.isbn?.firstOrNull(),
                        coverUrl = doc.coverId?.let { "https://covers.openlibrary.org/b/id/$it-L.jpg" },
                        readingStatus = ReadingStatus.WILL_READ,
                        personalRating = null,
                        personalReview = null,
                        dateStarted = null,
                        dateFinished = null,
                        genre = null,
                        pageCount = null
                    )
                } else {
                    null
                }
            }
            Result.success(mappedBooks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllBookshelves(): Flow<List<Bookshelf>> {
        return bookshelfDao.getAllBookshelves()
    }

    override suspend fun createBookshelf(name: String) {
        val newBookshelf = Bookshelf(name = name)
        bookshelfDao.insertBookshelf(newBookshelf)
    }

    override suspend fun addBookToBookshelf(bookId: Long, bookshelfId: Long) {
        val crossRef = BookBookshelfCrossRef(bookId = bookId, bookshelfId = bookshelfId)
        bookshelfDao.addBookToBookshelf(crossRef)
    }

    override fun getBookshelfWithBooks(bookshelfId: Long): Flow<BookshelfWithBooks?> {
        return bookshelfDao.getBookshelfWithBooks(bookshelfId)
    }
}
