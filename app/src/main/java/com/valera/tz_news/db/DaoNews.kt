package com.valera.tz_news.db

import androidx.room.*
import com.valera.tz_news.models.MyNews

@Dao
interface DaoNews {

    @Insert
    suspend fun insert(news: MyNews): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(listNews: List<MyNews>)

    @Update
    suspend fun update(news: MyNews): Int

    @Query("DELETE FROM MyNews")
    suspend fun deleteAll()

    @Query(value = "SELECT * FROM MyNews WHERE isHide = 0")
    suspend fun getNews() : MutableList<MyNews>

    @Query(value = "SELECT * FROM MyNews WHERE isHide = 1")
    suspend fun getHideNews() : MutableList<MyNews>
}