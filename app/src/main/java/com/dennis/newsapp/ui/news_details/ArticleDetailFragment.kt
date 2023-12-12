package com.dennis.newsapp.ui.news_details

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.dennis.newsapp.R
import com.dennis.newsapp.databinding.FragmentArticleDetailBinding
import com.dennis.newsapp.ui.NewsActivity
import com.dennis.newsapp.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class ArticleDetailFragment : Fragment(R.layout.fragment_article_detail) {

    lateinit var newsViewModel: NewsViewModel
    val args: ArticleDetailFragmentArgs by navArgs()
    lateinit var binding: FragmentArticleDetailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleDetailBinding.bind(view)

        newsViewModel = (activity as NewsActivity).newsViewModel
        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { url ->
                loadUrl(url)
            }
        }
        binding.fab.setOnClickListener {
            newsViewModel.saveNewsArticle(article)
            Snackbar.make(view, "Article saved for later read", Snackbar.LENGTH_LONG).show()
        }
    }

}