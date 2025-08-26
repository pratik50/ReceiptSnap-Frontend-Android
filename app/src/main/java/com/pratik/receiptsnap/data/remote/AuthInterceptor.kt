package com.pratik.receiptsnap.data.remote

import com.pratik.receiptsnap.data.local.UserPreferences
import jakarta.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor @Inject constructor(
    private val userPreferences: UserPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = userPreferences.getToken()
        val newRequest = chain.request().newBuilder()

        if (!token.isNullOrEmpty()) {
            newRequest.addHeader("Authorization", token)
        }

        return chain.proceed(newRequest.build())
    }
}