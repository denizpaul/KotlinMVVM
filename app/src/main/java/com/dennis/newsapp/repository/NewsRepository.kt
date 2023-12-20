package com.dennis.newsapp.repository

import com.dennis.newsapp.api.NewsApi
import com.dennis.newsapp.database.ArticleDatabase
import com.dennis.newsapp.models.Article
import javax.inject.Inject

open class NewsRepository @Inject constructor(private val newsApi: NewsApi, private val database: ArticleDatabase) {

    suspend fun getHeadlines(countryCode: String) =
        newsApi.getTopHeadlines(countryCode)

    suspend fun getHeadlinesFromSources(sources: String) =
        newsApi.getTopHeadlinesFromSources(sources)

    suspend fun upsert(article: Article) =
        database.getArticleDAO().upsert(article)

    suspend fun deleteArticle(article: Article) =
        database.getArticleDAO().deleteArticle(article)

    fun getSavedArticles() = database.getArticleDAO().getAllArticles()


    suspend fun getNewsSources(language: String) =
        newsApi.getNewsSources(language)
}