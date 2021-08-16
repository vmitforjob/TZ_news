package com.valera.tz_news.ui

import android.view.View
import com.valera.tz_news.models.MyNews

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClick(view: View, data: MyNews)
}