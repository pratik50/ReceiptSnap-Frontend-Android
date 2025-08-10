package com.pratik.receiptsnap.data.remote

import com.pratik.receiptsnap.data.remote.dto.AuthResponse
import com.pratik.receiptsnap.data.remote.dto.FilesResponse
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

    @GET("/api/files/dashboard")
    suspend fun getFiles(): FilesResponse

    @Multipart
    @POST("/api/files/upload")
    suspend fun uploadFile(@Part file: MultipartBody.Part): UploadFileResponse

    @DELETE("/api/files/{fileId}")
    suspend fun deleteFile(@Path("fileId") fileId: String)

}