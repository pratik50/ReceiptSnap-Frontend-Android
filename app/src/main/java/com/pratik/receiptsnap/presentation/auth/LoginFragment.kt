package com.pratik.receiptsnap.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pratik.receiptsnap.R
import com.pratik.receiptsnap.data.local.UserPreferences
import com.pratik.receiptsnap.databinding.FragmentLoginBinding
import com.pratik.receiptsnap.presentation.auth.state.AuthState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()
    private val userPreferences: UserPreferences by lazy {
        UserPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Idle -> {
                    // Handle idle state
                    binding.loginBtn.revertAnimation()
                }

                is AuthState.Loading -> {
                    // Handle loading state
                    binding.loginBtn.startAnimation()
                }

                is AuthState.Success -> {
                    // Handle success state
                    userPreferences.saveToken(state.token)
                    binding.loginBtn.revertAnimation()
                    findNavController().navigate(R.id.action_loginFragment_to_scanFragment)

                    Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                }

                is AuthState.Error -> {
                    // Handle error state
                    binding.loginBtn.revertAnimation()
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.txtSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.forgotPass.setOnClickListener {
            // Handle forgot email click
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.loginMail.text.toString()
            val password = binding.loginPass.text.toString()
            viewModel.login(email, password)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}