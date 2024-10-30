package com.malqaa.androidappp.newPhase.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.DialogFragment
import com.malqaa.androidappp.R

class WebViewPlayerDialogFragment : DialogFragment() {
    private lateinit var webView: WebView
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(ARG_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_web_view_player_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById(R.id.web_view)
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url ?: "")
    }

    companion object {
        private const val ARG_URL = "url"

        fun newInstance(url: String): WebViewPlayerDialogFragment {
            val fragment = WebViewPlayerDialogFragment()
            val args = Bundle().apply {
                putString(ARG_URL, url)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webView.destroy()
    }
}
