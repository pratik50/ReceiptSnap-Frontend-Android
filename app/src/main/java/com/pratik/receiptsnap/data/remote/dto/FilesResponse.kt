package com.pratik.receiptsnap.data.remote.dto

import com.pratik.receiptsnap.model.FileItem

data class FilesResponse(
    val folders: List<Any>, // or a Folder data class if needed
    val unfolderedFiles: List<FileItem>
)
