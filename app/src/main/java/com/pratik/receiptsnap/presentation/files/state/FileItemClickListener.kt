package com.pratik.receiptsnap.presentation.files.state

interface FileItemClickListener {
    fun onMoreClick(fileId: String)
    fun onFileClick(fileUrl: String, mimeType: String)
}