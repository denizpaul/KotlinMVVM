package com.dennis.newsapp.adpaters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dennis.newsapp.R
import com.dennis.newsapp.models.Source
import com.dennis.newsapp.ui.NewsViewModel

class SourcesAdapter() : RecyclerView.Adapter<SourcesAdapter.SourceViewHolder>() {

    inner class SourceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    private lateinit var sourceSelection: CheckBox
    private lateinit var sourceTitle: TextView
    private lateinit var sourceDescription: TextView
    private var onCheckedChangeListener: ((Source, Boolean) -> Unit)? = null

    private val differCallback = object: DiffUtil.ItemCallback<Source>() {
        override fun areItemsTheSame(oldItem: Source, newItem: Source): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Source, newItem: Source): Boolean {
            return oldItem == newItem && oldItem.isSelected == newItem.isSelected
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        return SourceViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news_source, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        val source = differ.currentList[position]
        sourceTitle = holder.itemView.findViewById(R.id.sourceName)
        sourceDescription = holder.itemView.findViewById(R.id.sourceDescription)
        sourceSelection = holder.itemView.findViewById(R.id.checkbox)
        sourceSelection.isChecked = source.isSelected
        holder.itemView.apply {
            sourceTitle.text = source.name
            sourceDescription.text = source.description
        }
        sourceSelection.setOnCheckedChangeListener { _, isChecked ->
            source.isSelected = isChecked
            onCheckedChangeListener?.let {
                it(source, isChecked)
            }
        }
    }

    fun setOnSelectedListener(listener: (Source, Boolean) -> Unit) {
        this.onCheckedChangeListener = listener
    }

}