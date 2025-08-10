package com.pratik.streamvault.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File


object CameraUtils {

    data class CameraData(val uri: Uri, val file: File, val intent: Intent)

    fun createImageFile(context: Context): File {
        val timestamp = System.currentTimeMillis()
        val storageDir = context.cacheDir
        return File.createTempFile("IMG_$timestamp", ".jpg", storageDir)
    }

    fun getUriForFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }

    fun prepareCameraData(context: Context): CameraData? {
        val file = createImageFile(context)
        val uri = getUriForFile(context, file)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }

        return if (intent.resolveActivity(context.packageManager) != null)
            CameraData(uri = uri, file = file, intent = intent)
        else null
    }
}

