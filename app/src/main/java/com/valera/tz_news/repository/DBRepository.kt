package com.valera.tz_news.repository

import com.valera.tz_news.db.AppDataBase
import com.valera.tz_news.models.MyNews

class DBRepository(private val db: AppDataBase) {

     suspend fun insert(news: MyNews) = db.newDao().insert(news)
     suspend fun insertAll(listNews: List<MyNews>) = db.newDao().insertAll(listNews)
     suspend fun update(news: MyNews) = db.newDao().update(news)
     suspend fun getNews() = db.newDao().getNews()
     suspend fun getHideNews() = db.newDao().getHideNews()

}