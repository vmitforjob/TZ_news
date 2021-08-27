package com.valera.tz_news

import android.app.Application
import com.valera.tz_news.api.ApiNews
import com.valera.tz_news.db.AppDataBase
import com.valera.tz_news.repository.DBRepository
import com.valera.tz_news.repository.NewsRepository

class App: Application() {
    lateinit var newsRepository : NewsRepository
    lateinit var dbRepository: DBRepository

    override fun onCreate() {
        super.onCreate()
        newsRepository = NewsRepository(ApiNews.invoke())
        dbRepository = DBRepository(AppDataBase.invoke(this))
    }
}