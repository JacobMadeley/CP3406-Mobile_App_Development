package com.jcu.jc428992.booktracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookshelves")
data class Bookshelf(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)
