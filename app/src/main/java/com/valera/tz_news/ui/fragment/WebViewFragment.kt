package com.valera.tz_news.ui.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.valera.tz_news.R

class WebViewFragment : Fragment(R.layout.fragment_web_view) {

    lateinit var newsUrl: String
    lateinit var webView: WebView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        webView = view.findViewById(R.id.webView)
        newsUrl =  arguments?.getString(MainFragment.NEWS_URL).toString()
        webView.apply {
            webViewClient = object : WebViewClient() {}
            loadUrl(newsUrl)
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