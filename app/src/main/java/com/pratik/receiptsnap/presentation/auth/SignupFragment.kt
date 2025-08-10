package com.pratik.receiptsnap.presentation.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pratik.receiptsnap.R
import com.pratik.receiptsnap.data.local.UserPreferences
import com.pratik.receiptsnap.databinding.FragmentSignupBinding
import com.pratik.receiptsnap.presentation.auth.state.AuthState
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()
    private val userPreferences: UserPreferences by lazy {
        UserPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Idle -> {
                    // Handle idle state
                    binding.signupBtn.revertAnimation()
                }
                is AuthState.Loading -> {
                    // Handle loading state
                    binding.signupBtn.startAnimation()
                }
                is AuthState.Success -> {
                    // Handle success state
                    userPreferences.saveToken(state.token)
                    binding.signupBtn.stopAnimation()
                    findNavController().navigate(R.id.action_signupFragment_to_scanFragment)

                    Toast.makeText(requireContext(), "SignUp successful", Toast.LENGTH_SHORT).show()
                }
                is AuthState.Error -> {
                    // Handle error state
                    binding.signupBtn.revertAnimation()
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.txtLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        binding.signupBtn.setOnClickListener {
            val name = binding.userName.text.toString()
            val email = binding.signupMail.text.toString()
            val password = binding.loginPass.text.toString()
            val confirmPass = binding.confirmLoginPass.text.toString()

            when {
                name.isEmpty() -> {
                    binding.userName.error = "Name required"
                }
                email.isEmpty() -> {
                    binding.signupMail.error = "Email required"
                }
                password.isEmpty() -> {
                    binding.loginPass.error = "Password required"
                }
                confirmPass.isEmpty() -> {
                    binding.confirmLoginPass.error = "Confirm your password"
                }
                password != confirmPass -> {
                    binding.confirmLoginPass.error = "Passwords do not match"
                }
                else -> {
                    viewModel.signup(name, email, password)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}