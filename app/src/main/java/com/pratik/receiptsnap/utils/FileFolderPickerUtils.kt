package com.pratik.receiptsnap.utils

import android.content.Intent

object FileFolderPickerUtils {
    fun getImagePickerIntent(): Intent {
        return Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*", "application/pdf"))
        }
    }
}