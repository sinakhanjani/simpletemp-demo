package com.simpletempco.simpletemp.ui.pages.common.web

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import com.simpletempco.simpletemp.databinding.ActivityWebViewBinding
import com.simpletempco.simpletemp.util.AppConfig.OPEN_DOCUMENT_BASE_URL

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url: String? = intent.getStringExtra("url")

        initViews()

        url?.let { openUrl(it) }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews() {

        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
        }

        binding.btnBack.setOnClickListener { finish() }

    }

    private fun openUrl(url: String) {

        val type = MimeTypeMap.getFileExtensionFromUrl(url)

        val mUrl = if (arrayOf("pdf", "doc", "docx").contains(type?.lowercase())) {
            OPEN_DOCUMENT_BASE_URL + url
        } else {
            url
        }

        binding.webView.loadUrl(mUrl)
    }

}