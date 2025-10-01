package com.jcu.jc428992.booktracker.data.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class BookshelfWithBooks(
    @Embedded val bookshelf: Bookshelf,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = BookBookshelfCrossRef::class,
            parentColumn = "bookshelfId",
            entityColumn = "bookId"
        )
    )
    val books: List<Book>
)
