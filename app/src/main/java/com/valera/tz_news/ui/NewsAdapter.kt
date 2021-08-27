package com.valera.tz_news.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.valera.tz_news.R
import com.valera.tz_news.databinding.ItemNewBinding
import com.valera.tz_news.models.MyNews

class NewsAdapter(
    private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(), Filterable {

    var news: MutableList<MyNews> = mutableListOf()
    var listFiltered: MutableList<MyNews> = mutableListOf()

    override fun getItemCount() = listFiltered.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NewsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_new,
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.recyclerviewNewsBinding.news = listFiltered[position]
        holder.recyclerviewNewsBinding.cardView.setOnClickListener {
            listener.onRecyclerViewItemClick(
                holder.recyclerviewNewsBinding.cardView,
                listFiltered[position]
            )
        }
        holder.recyclerviewNewsBinding.butHide.setOnClickListener {
            listener.onRecyclerViewItemClick(
                holder.recyclerviewNewsBinding.butHide,
                listFiltered[position]
            )
            val pos = position
            listFiltered.removeAt(pos)
            notifyDataSetChanged()
        }

    }

    fun updateData(_News:MutableList<MyNews>) {
        news = _News
        listFiltered = news
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(
        val recyclerviewNewsBinding: ItemNewBinding
    ) : RecyclerView.ViewHolder(recyclerviewNewsBinding.root)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val filterList : MutableList<MyNews> = mutableListOf()
                if (charSequence.toString().isEmpty()) {
                    filterList.addAll(news)
                } else {
                    for (row in news) {
                        if (row.title.toLowerCase()
                                .contains(charSequence.toString().toLowerCase()) || row.annotation.toLowerCase()
                                .contains(charSequence.toString().toLowerCase())
                        ) {
                            filterList.add(row)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                listFiltered = filterResults.values as MutableList<MyNews>
                notifyDataSetChanged()
            }
        }
    }

}
