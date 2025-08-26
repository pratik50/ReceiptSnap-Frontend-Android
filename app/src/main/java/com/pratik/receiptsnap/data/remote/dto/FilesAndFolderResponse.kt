package com.pratik.receiptsnap.data.remote.dto

import com.pratik.receiptsnap.model.FileItem
import com.pratik.receiptsnap.model.FolderItem

data class FilesAndFolderResponse(
    val folders: List<FolderItem>? = emptyList(),
    val files: List<FileItem>? = emptyList()
)
