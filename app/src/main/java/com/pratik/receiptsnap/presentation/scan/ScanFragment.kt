package com.pratik.receiptsnap.presentation.scan

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pratik.receiptsnap.R
import com.pratik.receiptsnap.data.local.UserPreferences
import com.pratik.receiptsnap.databinding.FragmentScanBinding
import com.pratik.receiptsnap.presentation.scan.state.UploadState
import com.pratik.receiptsnap.utils.CameraUtils
import com.pratik.receiptsnap.utils.FileFolderPickerUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ScanFragment : Fragment() {

    private lateinit var cameraUri: Uri
    private lateinit var cameraFile: File

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: ScanViewModel by viewModels()
    private val userPrefs: UserPreferences by lazy {
        UserPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Observe upload state for UI feedback
        viewmodel.uploadState.observe(viewLifecycleOwner) { event ->
            val state = event.getContentIfNotHandled() ?: return@observe

            when (state) {
                UploadState.Loading -> binding.btnUpload.isEnabled = false
                is UploadState.Success -> {
                    binding.btnUpload.isEnabled = true
                    Toast.makeText(requireContext(), "File uploaded successfully", Toast.LENGTH_SHORT).show()
                }
                is UploadState.Error -> {
                    binding.btnUpload.isEnabled = true
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
                UploadState.Idle -> Unit
            }
        }

        binding.apply {
            // Camera button listener
            btnScan.setOnClickListener {
                handleCameraPermission()
            }

            // Upload button listener
            btnUpload.setOnClickListener {
                showActionBottomSheet()
            }
        }
    }

    private fun showActionBottomSheet() {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialog.setContentView(R.layout.bottom_sheet_upload)
        dialog.show()

        val cameraButton = dialog.findViewById<LinearLayout>(R.id.btnCamera)
        val uploadButton = dialog.findViewById<LinearLayout>(R.id.btnUpload)
        val folderButton = dialog.findViewById<LinearLayout>(R.id.btnFolder)

        cameraButton?.setOnClickListener {
            handleCameraPermission()
            dialog.dismiss()
        }

        uploadButton?.setOnClickListener {
            val intent = FileFolderPickerUtils.getImagePickerIntent()
            filePickerLauncher.launch(intent)
            dialog.dismiss()
        }

        folderButton?.setOnClickListener {
            // Handle folder button click
            findNavController().navigate(R.id.action_scanFragment_to_signupFragment)
            Toast.makeText(requireContext(), "Folder button clicked", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

    }

    // FileManager launcher
    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data
        if (uri != null) {
            viewmodel.uploadFile(uri)
        } else {
            Toast.makeText(requireContext(), "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle camera permission
    private fun handleCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCameraFlow()
        } else {
            cameraPermissionRequestLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Camera permission launcher
    private val cameraPermissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCameraFlow()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Go to settings and enable camera permission to use this feature",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    // Launch camera when permission is granted
    private fun startCameraFlow() {
        val cameraData = CameraUtils.prepareCameraData(requireContext())
        if (cameraData != null) {
            cameraUri = cameraData.uri
            cameraFile = cameraData.file
            takePictureLauncher.launch(cameraData.intent)
        } else {
            Toast.makeText(requireContext(), "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }

    // Capture and upload image
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && ::cameraUri.isInitialized) {
                // Verify file exists and is not empty
                if (cameraFile.exists() && cameraFile.length() > 0) {
                    viewmodel.uploadFile(cameraUri)
                } else {
                    // Clean up empty file
                    cameraFile.delete()
                    Toast.makeText(
                        requireContext(),
                        "No image captured",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                // Clean up file on cancel
                if (::cameraFile.isInitialized) {
                    cameraFile.delete()
                }
            }
        }


    // Detaching the binding to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}