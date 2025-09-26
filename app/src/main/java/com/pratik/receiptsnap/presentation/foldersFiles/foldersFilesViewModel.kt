package com.pratik.receiptsnap.presentation.foldersFiles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratik.receiptsnap.data.local.UserPreferences
import com.pratik.receiptsnap.data.remote.dto.FilesAndFolderResponse
import com.pratik.receiptsnap.helper.Event
import com.pratik.receiptsnap.repository.FileAndFolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class FoldersFilesViewModel @Inject constructor(
    val fileAndFolderRepository: FileAndFolderRepository
): ViewModel()  {

    private val _files = MutableLiveData<FilesAndFolderResponse>()
    val files: LiveData<FilesAndFolderResponse> = _files

    private val _deleteState = MutableLiveData<Event<Boolean>>()
    val deleteState: LiveData<Event<Boolean>> = _deleteState

    private val _shareState = MutableLiveData<String?>()
    val shareState: LiveData<String?> = _shareState

    fun loadFiles(folderId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = fileAndFolderRepository.getFoldersFiles(folderId)
                _files.postValue(response)
            } catch (e: Exception) {
                Timber.e(e, "Error loading files")
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
            val success = fileAndFolderRepository.deleteFile(fileId)
            _deleteState.value = Event(success)
            if (success) {
                loadFiles(fileId)
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
