package com.pratik.receiptsnap.epoxy

import android.view.View
import android.view.ViewParent
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.pratik.receiptsnap.R
import com.pratik.receiptsnap.presentation.files.state.FileItemClickListener


class ItemFileModel(
    val fileNameValue: String,
    val fileSizeValue: String,
    val fileId: String,
    val fileUrl: String,
    val fileType: String,
    val clickListener: FileItemClickListener?
) : EpoxyModelWithHolder<ItemFileModel.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.item_file

    override fun bind(holder: Holder) {
        holder.fileName.text = fileNameValue
        holder.fileSize.text = fileSizeValue

        holder.menuIcon.setOnClickListener {
            clickListener?.onMoreClick(fileId)
        }

        holder.rootView.setOnClickListener {
            clickListener?.onFileClick(fileUrl, fileType)
        }
    }

    class Holder : EpoxyHolder() {
        lateinit var fileName: TextView
        lateinit var fileSize: TextView
        lateinit var menuIcon: ImageView
        lateinit var fileIcon: ImageView
        lateinit var rootView: View

        override fun bindView(itemView: View) {
            rootView = itemView
            fileName = itemView.findViewById(R.id.fileName)
            fileSize = itemView.findViewById(R.id.fileSize)
            fileIcon = itemView.findViewById(R.id.fileType_icon)
            menuIcon = itemView.findViewById(R.id.item_menu_icon)
        }
    }

    override fun createNewHolder(parent: ViewParent): Holder? {
        return Holder()
    }
}