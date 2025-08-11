package com.pratik.receiptsnap.data.remote.dto

import com.pratik.receiptsnap.model.FileItem

data class UploadFileResponse(
    val message: String,
    val file: FileItem
)