package com.dennis.newsapp.adpaters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dennis.newsapp.R
import com.dennis.newsapp.models.Article

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    lateinit var articleImage: ImageView
    lateinit var articleTitle: TextView
    lateinit var articleDescription: TextView
    lateinit var articlePublishedAt: TextView
    private var onNewsItemClickListener: ((Article) -> Unit)? = null

    private val differCallback = object: DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        articleTitle = holder.itemView.findViewById(R.id.articleTitle)
        articleDescription = holder.itemView.findViewById(R.id.articleDescription)
        articleImage = holder.itemView.findViewById(R.id.articleImage)
        articlePublishedAt = holder.itemView.findViewById(R.id.articlePublishedAt)

        holder.itemView.apply {
            articleTitle.text = article.title
            articleDescription.text = article.description
            articlePublishedAt.text = article.publishedAt
            Glide.with(this).load(article.urlToImage).into(articleImage)
        }
        holder.itemView.setOnClickListener {
            onNewsItemClickListener?.let {
                it(article)
            }
        }
    }

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        this.onNewsItemClickListener = listener
    }

}