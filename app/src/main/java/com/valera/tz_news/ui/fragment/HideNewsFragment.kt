package com.valera.tz_news.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valera.tz_news.R
import com.valera.tz_news.models.MyNews
import com.valera.tz_news.ui.*
import com.valera.tz_news.util.Resource


class HideNewsFragment : Fragment(R.layout.fragment_hide_news), RecyclerViewClickListener {

    private lateinit var recycler_view_hide_news: RecyclerView
    private val viewModel: HideNewsViewModel by viewModels { factory() }
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        recycler_view_hide_news = view.findViewById(R.id.recycler_view_hide_news)
        setupRecyclerView()

        viewModel.newsDB.observe(viewLifecycleOwner, { response ->
            when(response) {
                is Resource.Success -> {
                    recycler_view_hide_news.also {
                        newsAdapter.updateData(response.data!!)
                    }
                }
                is Resource.Error -> { }
                is Resource.Loading -> { }
            }

        })
    }

    fun setupRecyclerView() {
        recycler_view_hide_news.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.setHasFixedSize(true)
            it.adapter = NewsAdapter(this)
            newsAdapter = it.adapter as NewsAdapter
        }
    }

    override fun onRecyclerViewItemClick(view: View, data: MyNews) {
        when(view.id){
            R.id.cardView -> {
                val bundle = Bundle()
                bundle.putString(MainFragment.NEWS_URL, data.mobile_url)
                findNavController().navigate(
                    R.id.action_hideNewsFragment_to_webViewFragment,
                    bundle
                )
            }
            R.id.butHide -> {
                data.isHide = !data.isHide
                viewModel.updata(data)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}