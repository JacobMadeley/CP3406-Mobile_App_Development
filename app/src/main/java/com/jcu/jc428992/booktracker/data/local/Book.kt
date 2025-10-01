package com.jcu.jc428992.booktracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val author: String,
    val coverUrl: String?,
    val readingStatus: ReadingStatus,
    val personalRating: Float? = null,
    val personalReview: String? = null,
    val dateAdded: Long = System.currentTimeMillis(),
    val dateStarted: Long? = null,
    val dateFinished: Long? = null,
    val isbn: String? = null,
    val genre: String? = null,
    val pageCount: Int? = null
)
