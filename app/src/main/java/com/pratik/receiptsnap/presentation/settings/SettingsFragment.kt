package com.pratik.receiptsnap.presentation.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pratik.receiptsnap.R
import com.pratik.receiptsnap.data.local.UserPreferences
import com.pratik.receiptsnap.databinding.FragmentOrganizeBinding
import com.pratik.receiptsnap.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()
    private val userPrefs: UserPreferences by lazy {
        UserPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvLogout.setOnClickListener {
                viewModel.logout(userPrefs)
                findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
                Toast.makeText(requireContext(), "Logged Out", Toast.LENGTH_SHORT).show()
            }

            switchPreferences.setOnCheckedChangeListener { buttonView, isChecked ->

                if (isChecked) {
                    Toast.makeText(requireContext(), "Scan Quality (High)", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "Scan Quality (Low)", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            switchHelp.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    Toast.makeText(requireContext(), "Help & Support On", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Help & Support Off", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            switchNotifications.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    Toast.makeText(requireContext(), "Notifications On", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Notifications Off", Toast.LENGTH_SHORT).show()
                    switchOverWifiONly.isChecked = false
                    switchNewFeatureNotification.isChecked = false
                }
            }

            switchOverWifiONly.setOnCheckedChangeListener { buttonView, isChecked ->
                if (switchNotifications.isChecked) {
                    if (isChecked) {
                        Toast.makeText(
                            requireContext(),
                            "Sync Over Wi--Fi Only On",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Sync Over Wi-Fi Only Off",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    if (isChecked) { // if the user tried to turn it ON when notifications are OFF
                        Toast.makeText(
                            requireContext(),
                            "Enable Notifications first to use this feature.",
                            Toast.LENGTH_LONG
                        ).show()
                        switchOverWifiONly.isChecked = false // Force it back off
                    }
                }
            }

            switchNewFeatureNotification.setOnCheckedChangeListener { buttonView, isChecked ->
                if (switchNotifications.isChecked) {
                    // Handle new feature notification preference
                    if (isChecked) {
                        Toast.makeText(
                            requireContext(),
                            "New Feature Notifications On",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "New Feature Notifications Off",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // If main notifications are off, this should also be off
                    if (isChecked) { // if the user tried to turn it ON when notifications are OFF
                        Toast.makeText(
                            requireContext(),
                            "Enable Notifications first to use this feature.",
                            Toast.LENGTH_LONG
                        ).show()
                        switchNewFeatureNotification.isChecked = false // Force it back off
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}