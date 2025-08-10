package com.pratik.receiptsnap.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratik.receiptsnap.data.remote.dto.LoginRequest
import com.pratik.receiptsnap.data.remote.dto.SignupRequest
import com.pratik.receiptsnap.presentation.auth.state.AuthState
import com.pratik.receiptsnap.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.Idle)
    val authState: LiveData<AuthState> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = authRepository.login(LoginRequest(email, password))
                _authState.value = AuthState.Success(response.token)

                Timber.Forest.d("Login success: ${response.token}")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
                Timber.Forest.e("Login failed: ${e.message}")
            }
        }
    }

    fun signup(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = authRepository.signup(SignupRequest(name, email, password))
                _authState.value = AuthState.Success(response.token)

                Timber.Forest.d("Signup success: ${response.token}")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error")

                Timber.Forest.e("Signup failed: ${e.message}")
            }
        }
    }
}