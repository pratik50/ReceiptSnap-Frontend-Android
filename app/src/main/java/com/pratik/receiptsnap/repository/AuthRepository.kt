package com.pratik.receiptsnap.repository


import com.pratik.receiptsnap.data.remote.AuthApi
import com.pratik.receiptsnap.data.remote.dto.AuthResponse
import com.pratik.receiptsnap.data.remote.dto.LoginRequest
import com.pratik.receiptsnap.data.remote.dto.SignupRequest
import jakarta.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi
) {

    suspend fun login(request: LoginRequest): AuthResponse {
        return authApi.login(request)
    }

    suspend fun signup(request: SignupRequest): AuthResponse {
        return authApi.signup(request)
    }

}