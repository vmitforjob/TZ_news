package com.valera.tz_news.repository

import com.valera.tz_news.api.ApiNews

class NewsRepository(private val api: ApiNews) {

    suspend fun getNews() = api.getNews()

}