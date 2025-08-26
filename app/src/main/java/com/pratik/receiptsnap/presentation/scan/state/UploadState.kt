package com.pratik.receiptsnap.presentation.scan.state

import com.pratik.receiptsnap.data.remote.dto.UploadFileResponse

sealed class UploadState {
    object Idle : UploadState()
    object Loading : UploadState()
    data class Success(val uploadFileResponse: UploadFileResponse) : UploadState()
    data class Error(val message: String) : UploadState()
}