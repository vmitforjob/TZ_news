package com.valera.tz_news.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "MyNews")
data class MyNews (
    @PrimaryKey(autoGenerate = true) @SerializedName("id") val id: Int,
    @SerializedName("type") val type: Boolean,
    @SerializedName("title") val title: String,
    @SerializedName("img") val img: String,
    @SerializedName("news_date") val news_date: String,
    @SerializedName("news_date_uts") val news_date_uts: String,
    @SerializedName("annotation") val annotation: String,
    @SerializedName("mobile_url") val mobile_url: String,
    var isHide: Boolean = false
)