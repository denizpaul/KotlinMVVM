package com.dennis.newsapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dennis.newsapp.models.Article
import com.dennis.newsapp.models.NewsResponse
import com.dennis.newsapp.models.Source
import com.dennis.newsapp.models.SourceResponse
import com.dennis.newsapp.repository.NewsRepository
import com.dennis.newsapp.util.ScreenState
import com.dennis.newsapp.util.hasInternetConnection
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(application: Application, val newsRepository: NewsRepository) : AndroidViewModel(application) {

    var headlinesLiveData: MutableLiveData<ScreenState<NewsResponse>> = MutableLiveData()
    var newsSourcesLiveData: MutableLiveData<ScreenState<SourceResponse>> = MutableLiveData()
    private var headlinesResponse: NewsResponse? = null
    private val selectedItems = mutableListOf<Source>()

    fun getHeadlines(countryCode: String) = viewModelScope.launch {
        getAllHeadlines(countryCode)
    }

    fun getNewsSources() = viewModelScope.launch {
        fetchNewsSources()
    }

    private suspend fun getAllHeadlines(countryCode: String) {
        headlinesLiveData.postValue(ScreenState.Loading())
        try {
            val response: Response<NewsResponse>
            if (selectedItems.isNotEmpty()) {
                val sources = selectedItems.joinToString(",") { it.id }
                response = newsRepository.getHeadlinesFromSources(sources)
            } else {
                response = newsRepository.getHeadlines(countryCode)
            }
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

    private suspend fun fetchNewsSources() {
        newsSourcesLiveData.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection<Application>()) {
                val response = newsRepository.getNewsSources("en")
                newsSourcesLiveData.postValue(handleSourceNewsResponse(response))
            } else {
                newsSourcesLiveData.postValue(ScreenState.Error("Couldn't connect to server"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> newsSourcesLiveData.postValue(
                    ScreenState.Error(
                        t.message!!
                    )
                )
                else -> newsSourcesLiveData.postValue(
                    ScreenState.Error(
                        t.message!!
                    )
                )
            }
        }
    }
    private fun handleSourceNewsResponse(response: Response<SourceResponse>): ScreenState<SourceResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ScreenState.Success(resultResponse)
            }
        }
        return ScreenState.Error(response.message())
    }
    fun toggleSourceSelection(source: Source, isSelected: Boolean) {
        if (isSelected && !selectedItems.contains(source)) {
            selectedItems.add(source)
        } else {
            selectedItems.remove(source)
        }
    }
    fun getSelectedSources(): List<Source> {
        return selectedItems
    }

}