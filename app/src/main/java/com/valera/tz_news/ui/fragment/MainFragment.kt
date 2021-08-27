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

    private lateinit var recycler_view_news: RecyclerView
    private lateinit var viewModel: MainViewModel
    private lateinit var newsAdapter: NewsAdapter

    private var lastOffset = 0
    private var lastPosition:Int = 0

    private var showHideNewsText = ""

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

        showHideNewsText = getString(R.string.show_hidenews)

        recycler_view_news = view.findViewById(R.id.recycler_view_news)
        setupRecyclerView()

        val viewModelFactory = MainViewModelFactory(
            NewsRepository(ApiNews.invoke()),
            DBRepository(AppDataBase.invoke(requireContext()))
        )
        viewModel = ViewModelProvider(this,viewModelFactory).get(MainViewModel::class.java)
        viewModel.newsDB.observe(viewLifecycleOwner, { response ->
            when(response) {
                is Resource.Success -> {
                    recycler_view_news.also {
                        newsAdapter.updateData(response.data!!)
                    }
                    if (response.data?.isNotEmpty()!!) {
                        if (response.data[0].isHide)
                            showHideNewsText = getString(R.string.show_opennews)
                        else
                            showHideNewsText = getString(R.string.show_hidenews)
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
               if(showHideNewsText == getString(R.string.show_hidenews)) {
                   viewModel.getHideNewsDB()
                   item.title = getString(R.string.show_opennews)
                   showHideNewsText = getString(R.string.show_opennews)
                } else {
                    viewModel.getNewsDB()
                    item.title = getString(R.string.show_hidenews)
                    showHideNewsText = getString(R.string.show_hidenews)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_menu, menu)

        val itemHide = menu.findItem(R.id.show_hide)
        itemHide.title = showHideNewsText
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