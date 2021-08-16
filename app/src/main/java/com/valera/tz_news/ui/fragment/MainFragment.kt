package com.valera.tz_news.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valera.tz_news.R
import com.valera.tz_news.api.ApiNews
import com.valera.tz_news.db.AppDataBase
import com.valera.tz_news.models.MyNews
import com.valera.tz_news.repository.DBRepository
import com.valera.tz_news.repository.NewsRepository
import com.valera.tz_news.ui.MainViewModel
import com.valera.tz_news.ui.MainViewModelFactory
import com.valera.tz_news.ui.NewsAdapter
import com.valera.tz_news.ui.RecyclerViewClickListener
import com.valera.tz_news.util.Resource

class MainFragment : Fragment(R.layout.fragment_main), RecyclerViewClickListener {

    lateinit var recycler_view_news: RecyclerView
    lateinit private var viewModel: MainViewModel
    lateinit var newsAdapter: NewsAdapter

    var lastOffset = 0
    var lastPosition:Int = 0

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

        val viewModelFactory = MainViewModelFactory(
            NewsRepository(ApiNews.invoke()),
            DBRepository(AppDataBase.invoke(requireContext()))
        )

        viewModel = ViewModelProvider(this,viewModelFactory).get(MainViewModel::class.java)
        viewModel.getNewsDB()
        viewModel.newsDB.observe(viewLifecycleOwner, { response ->
            when(response) {
                is Resource.Success -> {
                    recycler_view_news.also {
                        it.layoutManager = LinearLayoutManager(requireContext())
                        it.setHasFixedSize(true)
                        it.adapter = NewsAdapter(response.data!!, this)
                        newsAdapter = it.adapter as NewsAdapter
                        (it.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(lastPosition, lastOffset)
                    }
                }
                is Resource.Error -> { }
                is Resource.Loading -> { }
            }

        })

    }

    override fun onPause() {
        super.onPause()
        val  topView = (recycler_view_news.layoutManager as LinearLayoutManager).getChildAt(0)
        if (topView != null)
        {
            lastOffset = topView.top
            lastPosition = (recycler_view_news.layoutManager as LinearLayoutManager).getPosition(topView)
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
                data.isHide = true
                viewModel.updata(data)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_update -> viewModel.getNewsApi()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_menu, menu)

        val item = menu.findItem(R.id.action_search)
        val searchView = item?.actionView as SearchView
        searchView.queryHint = "Нажмите здесь для поиска"
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