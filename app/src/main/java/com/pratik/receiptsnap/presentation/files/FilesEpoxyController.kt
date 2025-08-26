package com.pratik.receiptsnap.presentation.files

import com.airbnb.epoxy.EpoxyController
import com.pratik.receiptsnap.epoxy.ItemFileModel
import com.pratik.receiptsnap.model.FileItem
import com.pratik.receiptsnap.presentation.files.state.FileItemClickListener

class FilesEpoxyController : EpoxyController() {

    var clickListener: FileItemClickListener? = null

    var files: List<FileItem> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        files.forEach { file ->
            ItemFileModel(
                fileNameValue = file.name,
                fileSizeValue = if (file.size < 1024 * 1024) {
                    "${file.size / 1024} KB"
                } else {
                    "%.2f MB".format(file.size / 1024f / 1024f)
                },
                fileUrl = file.url,
                fileType = file.type,
                fileId = file.id,
                clickListener = clickListener
            ).id(file.id).addTo(this)
        }
    }
}