package com.dennis.newsapp.repository

import com.dennis.newsapp.api.RetrofitInstance
import com.dennis.newsapp.database.ArticleDatabase
import com.dennis.newsapp.models.Article

class NewsRepository(private val database: ArticleDatabase) {

    suspend fun getHeadlines(countryCode: String) =
        RetrofitInstance.newsApi.getTopHeadlines(countryCode)

    suspend fun upsert(article: Article) =
        database.getArticleDAO().upsert(article)

    suspend fun deleteArticle(article: Article) =
        database.getArticleDAO().deleteArticle(article)

    fun getSavedArticles() = database.getArticleDAO().getAllArticles()


}