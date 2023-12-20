package com.dennis.newsapp.api

import com.dennis.newsapp.util.ApiConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * TODO("Remove this once hilt is introduced")
 */
class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            //logging
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            //okhttp client builder
            val client = OkHttpClient.Builder()
                .addInterceptor(logging).build()
            //build retrofit instance
            Retrofit.Builder()
                    .baseUrl(ApiConstants.NEWS_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
        }

        val newsApi by lazy {
            retrofit.create(NewsApi::class.java)
        }
    }
}