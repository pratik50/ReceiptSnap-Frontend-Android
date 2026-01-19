package com.pratik.receiptsnap.presentation.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton
import com.pratik.receiptsnap.databinding.FragmentChangePasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        binding.btnUpdatePassword.setOnClickListener {
            val current = binding.etCurrentPassword.text.toString().trim()
            val newPass = binding.etNewPassword.text.toString().trim()
            val confirm = binding.etConfirmPassword.text.toString().trim()

            if (!validateInputs(current, newPass, confirm)) return@setOnClickListener

            // Start loading animation
            binding.btnUpdatePassword.startAnimation()

            // Simulate network call
            lifecycleScope.launch {
                delay(1500) // fake API delay
                binding.btnUpdatePassword.revertAnimation()

                // Success toast
                Toast.makeText(requireContext(), "Password updated successfully!", Toast.LENGTH_SHORT).show()

                // Optional: clear text fields
                binding.etCurrentPassword.text?.clear()
                binding.etNewPassword.text?.clear()
                binding.etConfirmPassword.text?.clear()
            }
        }
    }

    private fun validateInputs(current: String, newPass: String, confirm: String): Boolean {
        var valid = true

        // Current password
        if (current.isEmpty()) {
            binding.tilCurrent.error = "Enter your current password"
            valid = false
        } else binding.tilCurrent.error = null

        // New password
        if (newPass.length < 8) {
            binding.tilNew.error = "Password must be at least 8 characters"
            valid = false
        } else binding.tilNew.error = null

        // Confirm password
        if (confirm != newPass) {
            binding.tilConfirm.error = "Passwords do not match"
            valid = false
        } else binding.tilConfirm.error = null

        return valid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}