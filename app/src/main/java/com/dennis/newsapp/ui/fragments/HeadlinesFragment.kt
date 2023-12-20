package com.dennis.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dennis.newsapp.R
import com.dennis.newsapp.adpaters.NewsAdapter
import com.dennis.newsapp.databinding.FragmentHeadlinesBinding
import com.dennis.newsapp.ui.NewsActivity
import com.dennis.newsapp.ui.NewsViewModel
import com.dennis.newsapp.util.ScreenState

class HeadlinesFragment : Fragment(R.layout.fragment_headlines) {

    private var isLoading: Boolean = true
    private var isError: Boolean = false
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    lateinit var binding: FragmentHeadlinesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHeadlinesBinding.bind(view)
        (activity as AppCompatActivity).supportActionBar?.title = "Headlines"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        newsViewModel = (activity as NewsActivity).newsViewModel
        newsAdapter = NewsAdapter()
        binding.recyclerHeadlines.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        newsAdapter.setOnItemClickListener {
            val action = HeadlinesFragmentDirections.actionHeadlinesFragmentToArticleDetailFragment(it)
            findNavController().navigate(action)
        }
        newsViewModel.headlinesLiveData.observe(viewLifecycleOwner, Observer { screenState ->
            when(screenState) {
               is ScreenState.Success<*> -> {
                   hideProgressbar()
                   hideErrorMessage()
                   screenState.data?.let {
                       newsAdapter.differ.submitList(screenState.data.articles)
                   }
               }
               is ScreenState.Loading<*> ->
                   showProgressbar()
               is ScreenState.Error<*> -> {
                   hideProgressbar()
                   showErrorMessage("News fetching failed")
               }
               else -> {}
            }

        })
        newsViewModel.getHeadlines("us")
    }

    private fun showProgressbar() {
        binding.progressbar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressbar() {
        binding.progressbar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun hideErrorMessage(){
        binding.headlinesError.visibility = View.INVISIBLE
        binding.recyclerHeadlines.visibility = View.VISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String){
        binding.headlinesError.visibility = View.VISIBLE
        binding.errorText.text = message
        binding.recyclerHeadlines.visibility = View.INVISIBLE
        isError = true
    }
}