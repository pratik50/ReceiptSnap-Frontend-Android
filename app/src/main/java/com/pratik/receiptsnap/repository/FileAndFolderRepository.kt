package com.pratik.receiptsnap.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.pratik.receiptsnap.data.remote.FileApi
import com.pratik.receiptsnap.data.remote.dto.FilesAndFolderResponse
import com.pratik.receiptsnap.data.remote.dto.UploadFileResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.IOException

class FileAndFolderRepository @Inject constructor(
    val fileApi: FileApi,
    @ApplicationContext private val context: Context
) {

    suspend fun getFiles(): FilesAndFolderResponse {
        return fileApi.getFiles()
    }

    suspend fun getFolders(): FilesAndFolderResponse {
        return fileApi.getFolders()
    }

    suspend fun uploadFile(file: MultipartBody.Part): UploadFileResponse {
        return fileApi.uploadFile(file)
    }

    suspend fun deleteFile(fileId: String): Boolean {
        return try {
            fileApi.deleteFile(fileId)
            true
        } catch (e: Exception) {
            Timber.e("Delete error: ${e.message}")
            false
        }
    }


    // TODO: remove the complexity for this function and make it more readable
    suspend fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part =
        withContext(Dispatchers.IO) {

            val contentResolver = context.contentResolver

            // Determine mime type
            val mimeType = contentResolver.getType(fileUri)
                ?: throw IllegalArgumentException("Cannot determine MIME type for file")

            // ✅ Allow image, video, and PDF
            val isValidType = mimeType.startsWith("image/")
                    || mimeType.startsWith("video/")
                    || mimeType == "application/pdf"

            if (!isValidType) {
                throw IllegalArgumentException("Only image, video, or PDF files are allowed")
            }

            // Get file name
            val fileName = contentResolver.query(fileUri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                cursor.getString(nameIndex)
            } ?: "file_${System.currentTimeMillis()}"


            // Open input stream
            val inputStream = contentResolver.openInputStream(fileUri)
                ?: throw IOException("Invalid file URI")

            val bytes = inputStream.readBytes()
            inputStream.close()

            val requestFile = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
            MultipartBody.Part.createFormData(partName, fileName, requestFile)
        }
}