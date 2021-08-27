package com.valera.tz_news.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.valera.tz_news.models.MyNews
import com.valera.tz_news.models.RestData
import com.valera.tz_news.repository.DBRepository
import com.valera.tz_news.repository.NewsRepository
import com.valera.tz_news.util.Coroutines
import com.valera.tz_news.util.Resource
import kotlinx.coroutines.Job
import java.io.IOException

class MainViewModel(private val newsRepository: NewsRepository, private val dbRepository: DBRepository): ViewModel() {

    private lateinit var job: Job

    private val _news = MutableLiveData<Resource<RestData>>()
    val news: LiveData<Resource<RestData>>
        get() = _news

    private val _newsDB = MutableLiveData<Resource<MutableList<MyNews>>>()
    val newsDB: LiveData<Resource<MutableList<MyNews>>>
        get() = _newsDB

    init {
        getNewsDB()
    }

    fun getNewsApi() {
        _news.postValue(Resource.Loading())
        job = Coroutines.ioThenMain(
            { handleNewsApiResponse() },
            {
                when(it) {
                    is Resource.Success -> {
                        _news.postValue(it)
                        insertAllDB(it?.data?.data?.news!!)
                    }
                }
            }
        )
    }
    private suspend fun handleNewsApiResponse() =
        try {
            Resource.Success(newsRepository.getNews().body()!!)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> Resource.Error("Network Failure")
                else -> Resource.Error("Conversion Error")
            }
        }

    fun insertAllDB(listNews: List<MyNews>) {
        job = Coroutines.ioThenMain(
            { dbRepository.insertAll(listNews) },
            { getNewsDB() }
        )
    }

    fun getNewsDB() {
        _newsDB.postValue(Resource.Loading())
        job = Coroutines.ioThenMain(
            { handleNewsDBResponse()},
            {
                _newsDB.postValue(it)
                if(it?.data?.isEmpty()!! && news.value?.message?.length == null)
                    getNewsApi()
            }
        )
    }
    private suspend fun handleNewsDBResponse() =
        try {
            Resource.Success(dbRepository.getNews())
        } catch (t: Throwable) {
            when (t) {
                is IOException -> Resource.Error("DB Failure")
                else -> Resource.Error("Conversion Error")
            }
        }

    fun getHideNewsDB() {
        _newsDB.postValue(Resource.Loading())
        job = Coroutines.ioThenMain(
            { handleHideNewsDBResponse()},
            {
                _newsDB.postValue(it)
            }
        )
    }
    private suspend fun handleHideNewsDBResponse() =
        try {
            Resource.Success(dbRepository.getHideNews())
        } catch (t: Throwable) {
            when (t) {
                is IOException -> Resource.Error("DB Failure")
                else -> Resource.Error("Conversion Error")
            }
        }

    fun updata(news: MyNews) {
        job = Coroutines.ioThenMain(
            { dbRepository.update(news) },
            {  }
        )

    }

    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}