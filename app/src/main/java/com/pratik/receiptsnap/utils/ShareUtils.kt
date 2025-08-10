package com.pratik.receiptsnap.utils

import android.content.Context
import android.content.Intent

class ShareUtils {
    fun shareTextLink(context: Context, link: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Share file link via")
        context.startActivity(shareIntent)
    }
}