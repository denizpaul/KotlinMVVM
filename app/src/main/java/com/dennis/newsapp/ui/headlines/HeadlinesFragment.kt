package com.dennis.newsapp.ui.headlines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var binding: FragmentHeadlinesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHeadlinesBinding.bind(view)

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
                   screenState.data?.let {
                       newsAdapter.differ.submitList(screenState.data.articles)
                   }
               }
               is ScreenState.Loading<*> ->
                   showProgressbar()
               is ScreenState.Error<*> -> {
                   hideProgressbar()
                   Toast.makeText(context, "News fetching failed", Toast.LENGTH_LONG).show()
               }
               else -> {}
            }

        })

    }

    private fun showProgressbar() {
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressbar() {
        binding.progressBar.visibility = View.INVISIBLE
        isLoading = false
    }
}