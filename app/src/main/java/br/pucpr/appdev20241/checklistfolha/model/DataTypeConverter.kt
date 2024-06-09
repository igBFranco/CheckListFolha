package br.pucpr.appdev20241.checklistfolha.model

import androidx.room.TypeConverter
import java.util.Date

class DataTypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}