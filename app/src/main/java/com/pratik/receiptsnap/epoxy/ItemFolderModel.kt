package com.pratik.receiptsnap.epoxy

import android.util.Log
import android.view.View
import android.view.ViewParent
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.pratik.receiptsnap.R
import com.pratik.receiptsnap.presentation.files.state.FileItemClickListener

class ItemFolderModel(
    val folderName: String,
    val folderId: String,
    val clickListener: FileItemClickListener?,
    val bgColor: Int
) : EpoxyModelWithHolder<ItemFolderModel.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.item_folder

    override fun bind(holder: Holder) {
        holder.folderName.text = folderName.replace("_", "\n")

        Log.d("bgcolour", "bind: $bgColor")
        val colorInt = ContextCompat.getColor(holder.rootView.context, bgColor)
        holder.contentLayout.setBackgroundColor(colorInt)
        holder.rootView.setOnClickListener {
            //clickListener?.onFileClick(folderId)
        }
    }

    class Holder : EpoxyHolder() {
        lateinit var folderName: TextView
        lateinit var rootView: View
        lateinit var contentLayout: View

        override fun bindView(itemView: View) {
            rootView = itemView
            folderName = itemView.findViewById(R.id.folderName)
            contentLayout = itemView.findViewById(R.id.folderContent) // naya id

        }
    }

    override fun createNewHolder(parent: ViewParent): Holder? {
        return Holder()
    }
}