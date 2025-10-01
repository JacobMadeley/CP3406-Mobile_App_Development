package com.jcu.jc428992.booktracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters // Make sure this import is present

@Database(
    entities = [Book::class, Bookshelf::class, BookBookshelfCrossRef::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun bookshelfDao(): BookshelfDao
}
