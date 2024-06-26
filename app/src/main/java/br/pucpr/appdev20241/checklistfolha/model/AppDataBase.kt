package br.pucpr.appdev20241.checklistfolha.model

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context

@Database(entities = [ToDo::class, Quadro::class, Fechamento::class], version = 2)
@TypeConverters(DataTypeConverter::class)
abstract class AppDataBase: RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
    abstract fun quadroDao(): QuadroDao
    abstract fun fechamentoDao(): FechamentoDao


    companion object {
        @Volatile private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}