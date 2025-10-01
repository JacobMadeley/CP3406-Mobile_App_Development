package com.jcu.jc428992.booktracker.data.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromReadingStatus(status: ReadingStatus): String {
        return status.name
    }

    @TypeConverter
    fun toReadingStatus(statusString: String): ReadingStatus {
        return ReadingStatus.valueOf(statusString)
    }
}
