package com.pratik.receiptsnap.presentation.organize.state

interface FileItemClickListener {
    fun onMoreClick(fileId: String)
    fun onFileClick(fileUrl: String, mimeType: String)
}