package com.valera.tz_news.api

import com.valera.tz_news.models.RestData
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiNews {

    @GET("mobile/news/list")
    suspend fun getNews(): Response<RestData>

    companion object{
        operator fun invoke() : ApiNews {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://ws-tszh-1c-test.vdgb-soft.ru/api/")
                .build()
                .create(ApiNews::class.java)
        }
    }
}