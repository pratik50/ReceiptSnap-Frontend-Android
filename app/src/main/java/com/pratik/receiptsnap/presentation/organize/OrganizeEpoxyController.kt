package com.pratik.receiptsnap.presentation.organize

import com.airbnb.epoxy.EpoxyController
import com.pratik.receiptsnap.R
import com.pratik.receiptsnap.epoxy.ItemFolderModel
import com.pratik.receiptsnap.model.FolderItem
import com.pratik.receiptsnap.presentation.files.state.FileItemClickListener

class OrganizeEpoxyController : EpoxyController() {

    var clickListener: FileItemClickListener? = null

    var folders: List<FolderItem> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }


    // fix color pool
    private val colors = listOf(
        R.color.red_200,
        R.color.blue_grey_200,
        R.color.purple_200,
        R.color.yellow_200,
        R.color.cyan_200,
        R.color.pink_200,
        R.color.deep_orange_200,
        R.color.teal_200,
        R.color.light_blue_200,
        R.color.deep_purple_200,
        R.color.lime_200,
        R.color.amber_200,
    )

    override fun buildModels() {
        folders.forEachIndexed { index, folder ->
            ItemFolderModel(
                folderName = folder.name,
                folderId = folder.id,
                clickListener = clickListener,
                bgColor = colors[index % colors.size] // ab index milega
            ).id(folder.id).addTo(this)
        }
    }
}