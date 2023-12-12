package com.dennis.newsapp.api

import com.dennis.newsapp.models.NewsResponse
import com.dennis.newsapp.util.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country")
        country: String = "us",
        @Query("apiKey")
        apiKey: String = ApiConstants.NEWS_API_KEY
    ):Response<NewsResponse>
}