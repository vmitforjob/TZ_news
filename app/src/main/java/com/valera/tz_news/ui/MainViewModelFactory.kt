package com.valera.tz_news.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.valera.tz_news.App
import java.lang.IllegalStateException

class MainViewModelFactory(
    private val app: App,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass) {
            MainViewModel::class.java -> {
                MainViewModel(app.newsRepository, app.dbRepository)
            }
            HideNewsViewModel::class.java -> {
                HideNewsViewModel(app.dbRepository)
            }
            else -> throw IllegalStateException("Unknown view model class")
        }
        return viewModel as T
    }
}

fun Fragment.factory() = MainViewModelFactory(requireContext().applicationContext as App)