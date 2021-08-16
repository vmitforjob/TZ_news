package com.valera.tz_news.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.valera.tz_news.repository.DBRepository
import com.valera.tz_news.repository.NewsRepository
import java.lang.IllegalStateException

class MainViewModelFactory(
    private val newsRepository: NewsRepository,
    private val dbRepository: DBRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass) {
            MainViewModel::class.java -> {
                MainViewModel(newsRepository,dbRepository)
            }
            else -> throw IllegalStateException("Unknown view model class")
        }
        return viewModel as T
    }
}