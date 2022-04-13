package com.breaktime.lab2.view.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.breaktime.lab2.R
import com.breaktime.lab2.databinding.FragmentWebBinding

class WebFragment : Fragment() {
    private lateinit var binding: FragmentWebBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentWebBinding?>(
            inflater, R.layout.fragment_web, container, false
        ).apply {
            web.webViewClient = WebViewClient()
            val path = (arguments?.getString("path") ?: "").toString()
            web.loadUrl(path)
        }
        return binding.root
    }
}