package com.pratik.receiptsnap.presentation.scan

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratik.receiptsnap.data.local.UserPreferences
import com.pratik.receiptsnap.data.remote.dto.FilesAndFolderResponse
import com.pratik.receiptsnap.helper.Event
import com.pratik.receiptsnap.presentation.scan.state.UploadState
import com.pratik.receiptsnap.repository.FileAndFolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ScanViewModel @Inject constructor(
    val fileRepository: FileAndFolderRepository
): ViewModel() {

    private val _files = MutableLiveData<FilesAndFolderResponse>()
    val files: LiveData<FilesAndFolderResponse> = _files

    private val _uploadState = MutableLiveData<Event<UploadState>>(Event(UploadState.Idle))
    val uploadState: LiveData<Event<UploadState>> get() = _uploadState

    fun uploadFile(uri: Uri) {
        _uploadState.postValue(Event(UploadState.Loading))
        viewModelScope.launch {
            try {
                val filePart = fileRepository.prepareFilePart("file", uri)
                val response = fileRepository.uploadFile(filePart)

                _uploadState.value = Event(UploadState.Success(response))
            } catch (e: Exception) {
                Timber.Forest.e("Upload error: ${e.message}")
                _uploadState.postValue(Event(UploadState.Error(e.message ?: "Upload failed")))
            }
        }
    }

    //  Logout by clearing token
    fun logout(userPrefs: UserPreferences) {
        viewModelScope.launch {
            userPrefs.clearToken()
        }
    }

}