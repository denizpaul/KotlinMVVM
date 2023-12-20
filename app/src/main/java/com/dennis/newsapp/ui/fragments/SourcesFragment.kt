package com.dennis.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dennis.newsapp.R
import com.dennis.newsapp.adpaters.SourcesAdapter
import com.dennis.newsapp.databinding.FragmentSourceBinding
import com.dennis.newsapp.ui.NewsActivity
import com.dennis.newsapp.ui.NewsViewModel
import com.dennis.newsapp.util.ScreenState

class SourcesFragment : Fragment(R.layout.fragment_source) {

    private var isLoading: Boolean = true
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var sourcesAdapter: SourcesAdapter
    private lateinit var binding: FragmentSourceBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSourceBinding.bind(view)
        (activity as AppCompatActivity).supportActionBar?.title = "Select sources"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        newsViewModel = (activity as NewsActivity).newsViewModel
        sourcesAdapter = SourcesAdapter()
        binding.recyclerNewsSources.apply {
            adapter = sourcesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        sourcesAdapter.setOnSelectedListener { source, isSelected ->
            run {
                newsViewModel.toggleSourceSelection(source, isSelected)
            }
        }
        newsViewModel.newsSourcesLiveData.observe(viewLifecycleOwner, Observer { screenState ->
            when(screenState) {
                is ScreenState.Success<*> -> {
                    hideProgressbar()
                    screenState.data?.sources?.forEach { source ->
                        source.isSelected = newsViewModel.getSelectedSources().any { it.url == source.url }
                    }
                    screenState.data?.let {
                        sourcesAdapter.differ.submitList(screenState.data.sources)
                    }
                }
                is ScreenState.Loading<*> ->
                    showProgressbar()
                is ScreenState.Error<*> -> {
                    hideProgressbar()
                    Toast.makeText(context, "Sources fetching failed", Toast.LENGTH_LONG).show()
                }
                else -> {}
            }

        })
        newsViewModel.getNewsSources()
    }

    private fun showProgressbar() {
        binding.progressbar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressbar() {
        binding.progressbar.visibility = View.INVISIBLE
        isLoading = false
    }


}