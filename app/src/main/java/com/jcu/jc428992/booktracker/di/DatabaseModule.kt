package com.jcu.jc428992.booktracker.di

import android.content.Context
import androidx.room.Room
import com.jcu.jc428992.booktracker.data.local.AppDatabase
import com.jcu.jc428992.booktracker.data.local.BookDao
import com.jcu.jc428992.booktracker.data.local.BookshelfDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "book_tracker_db"
        ).build()
    }
    @Provides
    fun provideBookDao(database: AppDatabase): BookDao {
        return database.bookDao()
    }

    @Provides
    fun provideBookshelfDao(database: AppDatabase): BookshelfDao {
        return database.bookshelfDao()
    }
}
