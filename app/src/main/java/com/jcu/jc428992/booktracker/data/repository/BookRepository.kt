package com.jcu.jc428992.booktracker.data.repository

import kotlinx.coroutines.flow.Flow
import com.jcu.jc428992.booktracker.data.local.Book
import com.jcu.jc428992.booktracker.data.local.Bookshelf
import com.jcu.jc428992.booktracker.data.local.BookshelfWithBooks

interface BookRepository {
    fun getAllBooks(): Flow<List<Book>>

    fun getBookById(id: Long): Flow<Book?>

    suspend fun addBook(book: Book)

    suspend fun updateBook(book: Book)

    suspend fun deleteBook(book: Book)

    suspend fun searchBooksOnline(query: String, searchType: SearchType): Result<List<Book>>

    fun getAllBookshelves(): Flow<List<Bookshelf>>

    suspend fun createBookshelf(bookshelf: String)

    suspend fun addBookToBookshelf(bookId: Long, bookshelfId: Long)

    fun getBookshelfWithBooks(bookshelfId: Long): Flow<BookshelfWithBooks?>

    suspend fun deleteBookshelf(bookshelf: Bookshelf)
}
