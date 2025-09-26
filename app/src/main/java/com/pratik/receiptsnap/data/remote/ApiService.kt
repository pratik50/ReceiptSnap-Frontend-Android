package com.pratik.receiptsnap.data.remote

import com.pratik.receiptsnap.data.remote.dto.AuthResponse
import com.pratik.receiptsnap.data.remote.dto.FilesAndFolderResponse
import com.pratik.receiptsnap.data.remote.dto.LoginRequest
import com.pratik.receiptsnap.data.remote.dto.SignupRequest
import com.pratik.receiptsnap.data.remote.dto.UploadFileResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface AuthApi {
    @POST("/api/auth/signup")
    suspend fun signup(@Body request: SignupRequest): AuthResponse

    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse
}


interface FileApi {

    @GET("/api/files/getAllFiles")
    suspend fun getFiles(): FilesAndFolderResponse

    @GET("/api/files/getAllFolders")
    suspend fun getFolders(): FilesAndFolderResponse

    @Multipart
    @POST("/api/files/upload")
    suspend fun uploadFile(@Part file: MultipartBody.Part): UploadFileResponse

    @DELETE("/api/files/{fileId}")
    suspend fun deleteFile(@Path("fileId") fileId: String)

    @GET("/api/files/{folderId}/files")
    suspend fun getFoldersFiles(@Path("folderId") folderId: String): FilesAndFolderResponse
}