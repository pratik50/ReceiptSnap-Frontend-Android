package com.pratik.receiptsnap.presentation.organize

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratik.receiptsnap.data.local.UserPreferences
import com.pratik.receiptsnap.data.remote.dto.FilesResponse
import com.pratik.receiptsnap.presentation.organize.state.UploadState
import com.pratik.receiptsnap.repository.FileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class OrganizeViewModel @Inject constructor(
    val fileRepository: FileRepository
): ViewModel() {

    private val _files = MutableLiveData<FilesResponse>()
    val files: LiveData<FilesResponse> = _files

    private val _uploadState = MutableLiveData<UploadState>()
    val uploadState: LiveData<UploadState> get() = _uploadState

    private val _deleteState = MutableLiveData<Boolean>()
    val deleteState: LiveData<Boolean> = _deleteState

    private val _shareState = MutableLiveData<String?>()
    val shareState: LiveData<String?> = _shareState

    fun loadFiles() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = fileRepository.getFiles()
                _files.postValue(response)
            } catch (e: Exception) {
                Timber.e(e, "Error loading files")
            }
        }
    }

    fun uploadFile(uri: Uri) {
        _uploadState.postValue(UploadState.Loading)
        viewModelScope.launch {
            try {
                val filePart = fileRepository.prepareFilePart("file", uri)
                val response = fileRepository.uploadFile(filePart)

                _uploadState.value = UploadState.Success(response)
            } catch (e: Exception) {
                Timber.Forest.e("Upload error: ${e.message}")
                _uploadState.postValue(UploadState.Error(e.message ?: "Upload failed"))
            }
        }
    }

    //  Logout by clearing token
    fun logout(userPrefs: UserPreferences) {
        viewModelScope.launch {
            userPrefs.clearToken()
        }
    }

    fun deleteFile(fileId: String) {
        viewModelScope.launch {
            val success = fileRepository.deleteFile(fileId)
            _deleteState.value = success
            if (success) {
                loadFiles()
            }
        }
    }

//    fun generateShareLink(fileId: String) {
//        viewModelScope.launch {
//            try {
//                val response = fileRepository.generateShareLink(fileId)
//                _shareState.value = response.link
//            } catch (e: Exception) {
//                Timber.e(e, "Share link failed")
//                _shareState.value = null
//            }
//        }
//    }

    fun clearShareState() {
        _shareState.value = null
    }

    /*

    fun downloadFile(fileUrl: String, fileName: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(fileUrl).build()
                val client = OkHttpClient()
                val response = client.newCall(request).execute()
                val input = response.body?.byteStream()
                val file = File(context.getExternalFilesDir(null), fileName)
                val output = FileOutputStream(file)
                input?.copyTo(output)
                output.close()

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Downloaded to ${file.absolutePath}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Timber.e(e, "Download failed")
            }
        }
    }*/
}