package com.jcu.jc428992.booktracker.data.local

import androidx.room.Entity

@Entity(tableName = "book_bookshelf_cross_ref", primaryKeys = ["bookId", "bookshelfId"])
data class BookBookshelfCrossRef(
    val bookId: Long,
    val bookshelfId: Long
)
