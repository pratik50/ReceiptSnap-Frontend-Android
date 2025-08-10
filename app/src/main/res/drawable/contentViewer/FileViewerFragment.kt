package com.pratik.streamvault.presentation.contentViewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.pratik.streamvault.databinding.FragmentFileViewerBinding
import com.pratik.streamvault.utils.NetworkConstants
import timber.log.Timber
import java.net.URLEncoder

class FileViewerFragment : Fragment() {

    private lateinit var binding: FragmentFileViewerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFileViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val fileUrl = arguments?.getString("fileUrl")!!
        val mimeType = arguments?.getString("mimeType")!!

        val fileFullUrl = "${NetworkConstants.BASE_URL}$fileUrl"

        if (mimeType.startsWith("image/")) {
            binding.imageView.visibility = View.VISIBLE
            Glide.with(this).load(fileFullUrl).into(binding.imageView)

        } else if (mimeType == "application/pdf") {
            binding.pdfView.visibility = View.VISIBLE

            val encodedUrl = URLEncoder.encode(fileFullUrl, "UTF-8")
            val requiredFullUrl = "${NetworkConstants.BASE_URL}/pdfjs/pdf_preview_loader.html?file=$encodedUrl"

            binding.pdfView.apply {
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) = false
                }
                settings.apply {
                    javaScriptEnabled = true
                    allowFileAccess = true
                    allowContentAccess = true
                    domStorageEnabled = true
                    builtInZoomControls = true
                    displayZoomControls = false
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }
                setLayerType(View.LAYER_TYPE_HARDWARE, null)
                loadUrl(requiredFullUrl)
            }
        }
    }
}