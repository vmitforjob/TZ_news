package com.valera.tz_news.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valera.tz_news.R
import com.valera.tz_news.models.MyNews
import com.valera.tz_news.ui.*
import com.valera.tz_news.util.Resource

class MainFragment : Fragment(R.layout.fragment_main), RecyclerViewClickListener {

    private lateinit var recycler_view_news: RecyclerView
    private val viewModel: MainViewModel by viewModels { factory() }
    private lateinit var newsAdapter: NewsAdapter

    companion object {
        const val NEWS_URL = "NEWS_URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        recycler_view_news = view.findViewById(R.id.recycler_view_news)
        setupRecyclerView()

        viewModel.newsDB.observe(viewLifecycleOwner, { response ->
            when(response) {
                is Resource.Success -> {
                    recycler_view_news.also {
                        newsAdapter.updateData(response.data!!)
                    }
                }
                is Resource.Error -> { }
                is Resource.Loading -> { }
            }
        })

    }

    fun setupRecyclerView() {
        recycler_view_news.also {
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
                bundle.putString(NEWS_URL, data.mobile_url)
                findNavController().navigate(
                    R.id.action_mainFragment_to_webViewFragment,
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
            R.id.item_update -> viewModel.getNewsApi()
            R.id.show_hide -> {
                findNavController().navigate(
                    R.id.action_mainFragment_to_hideNewsFragment
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_menu, menu)

        val item = menu.findItem(R.id.action_search)
        val searchView = item?.actionView as SearchView
        searchView.queryHint = getString(R.string.txt_search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newsAdapter.filter.filter(newText)
                return false
            }

        })
        return super.onCreateOptionsMenu(menu, inflater)
    }

}