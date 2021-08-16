package com.valera.tz_news.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.valera.tz_news.models.MyNews

@Database(entities = [MyNews::class], version = 4, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun newDao(): DaoNews

    companion object {
        @Volatile
        private var instance: AppDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "news_db"
            ).fallbackToDestructiveMigration()
                .build()
    }
}