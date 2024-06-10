package br.pucpr.appdev20241.checklistfolha.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
    @TypeConverter
    fun fromJsonToListToDo(json: String): List<ToDo> {
        val listType = object : TypeToken<List<ToDo>>() {}.type
        return Gson().fromJson(json, listType)
    }

    @TypeConverter
    fun fromListToDoToJson(list: List<ToDo>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromJsonToListQuadro(json: String): List<Quadro> {
        val listType = object : TypeToken<List<Quadro>>() {}.type
        return Gson().fromJson(json, listType)
    }

    @TypeConverter
    fun fromListQuadroToJson(list: List<Quadro>): String {
        return Gson().toJson(list)
    }
}