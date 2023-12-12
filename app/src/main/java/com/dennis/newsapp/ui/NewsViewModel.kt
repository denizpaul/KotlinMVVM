package com.dennis.newsapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dennis.newsapp.models.Article
import com.dennis.newsapp.models.NewsResponse
import com.dennis.newsapp.repository.NewsRepository
import com.dennis.newsapp.util.ScreenState
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(application: Application, val newsRepository: NewsRepository) : AndroidViewModel(application) {

    var headlinesLiveData: MutableLiveData<ScreenState<NewsResponse>> = MutableLiveData()
    var headlinesResponse: NewsResponse? = null

    init {
        getHeadlines("US")
    }

    fun getHeadlines(countryCode: String) = viewModelScope.launch {
        getAllHeadlines(countryCode)
    }

    suspend fun getAllHeadlines(countryCode: String) {
        try {
            val response = newsRepository.getHeadlines(countryCode)
            headlinesLiveData.postValue(handleHeadlinesResponse(response))
        } catch (t: Throwable) {
            when(t) {
                is IOException -> headlinesLiveData.postValue(ScreenState.Error("Couldn't connect to server"))
                else -> headlinesLiveData.postValue(ScreenState.Error("No Signal"))
            }
        }

    }
    private fun handleHeadlinesResponse(response: Response<NewsResponse>): ScreenState<NewsResponse> {
        if(response.isSuccessful) {
            response.body().let { newsHeadlinesResponse ->
                headlinesResponse =  newsHeadlinesResponse
                return ScreenState.Success(headlinesResponse!!)
            }
        }
        return ScreenState.Error(response.message())
    }
    fun saveNewsArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun deleteNewsArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun getSavedArticles() = newsRepository.getSavedArticles()
}