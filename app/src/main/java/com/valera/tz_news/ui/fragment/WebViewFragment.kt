package com.valera.tz_news.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.valera.tz_news.R

class WebViewFragment : Fragment(R.layout.fragment_web_view) {

    lateinit var newsUrl: String
    lateinit var webView: WebView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_web_view, container, false)
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

        return view
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