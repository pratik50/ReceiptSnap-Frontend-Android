package com.pratik.receiptsnap.model

data class FileItem(
    val id: String,
    val name: String,
    val url: String,
    val type: String,
    val size: Long,
    val userId: String,
    val folderId: String?,
    val createdAt: String
)