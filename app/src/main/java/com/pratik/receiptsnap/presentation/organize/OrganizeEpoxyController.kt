package com.pratik.receiptsnap.presentation.organize

import com.airbnb.epoxy.EpoxyController
import com.pratik.receiptsnap.epoxy.ItemFileModel
import com.pratik.receiptsnap.epoxy.ItemFolderModel
import com.pratik.receiptsnap.model.FileItem
import com.pratik.receiptsnap.model.FolderItem
import com.pratik.receiptsnap.presentation.files.state.FileItemClickListener
import kotlin.collections.forEach

class OrganizeEpoxyController : EpoxyController() {

    var clickListener: FileItemClickListener? = null

    var folders: List<FolderItem> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        folders.forEach { folder ->
            ItemFolderModel(
                folderName = folder.name,
                folderId = folder.id,
                clickListener = clickListener
            ).id(folder.id).addTo(this)
        }
    }
}

//
//class FilesEpoxyController : EpoxyController() {
//
//    var clickListener: FileItemClickListener? = null
//
//    var folders: List<FolderItem> = emptyList()
//        set(value) {
//            field = value
//            requestModelBuild()
//        }
//
//    override fun buildModels() {
//        folders.forEach { folder ->
//            ItemFolderModel(
//                folderName = folder.name,
//                folderId = folder.id,
//                clickListener = clickListener
//            ).id(folder.id).addTo(this)
//        }
//    }
//}