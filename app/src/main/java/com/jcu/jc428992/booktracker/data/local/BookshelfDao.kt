package com.jcu.jc428992.booktracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface BookshelfDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBookshelf(bookshelf: Bookshelf)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBookToBookshelf(crossRef: BookBookshelfCrossRef)

    @Query("SELECT * FROM bookshelves ORDER BY name ASC")
    fun getAllBookshelves(): Flow<List<Bookshelf>>

    @Transaction
    @Query("SELECT * FROM bookshelves WHERE id = :bookshelfId")
    fun getBookshelfWithBooks(bookshelfId: Long): Flow<BookshelfWithBooks>

    @Delete
    suspend fun deleteBookshelf(bookshelf: Bookshelf)
}
