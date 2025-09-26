package com.pratik.receiptsnap.presentation.foldersFiles

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pratik.receiptsnap.R
import com.pratik.receiptsnap.databinding.FragmentFilesBinding
import com.pratik.receiptsnap.databinding.FragmentFoldersFilesBinding
import com.pratik.receiptsnap.presentation.files.FilesEpoxyController
import com.pratik.receiptsnap.presentation.files.FilesFragmentDirections
import com.pratik.receiptsnap.presentation.files.FilesViewModel
import com.pratik.receiptsnap.presentation.files.state.FileItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoldersFilesFragment : Fragment() {

    private var _binding: FragmentFoldersFilesBinding?= null
    private val binding get() = _binding!!
    private val viewmodel: FoldersFilesViewModel by viewModels()
    private lateinit var epoxyController: FilesEpoxyController
    private val args: FoldersFilesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoldersFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Epoxy Initialization
        epoxyController = FilesEpoxyController()
        binding.epoxyRecyclerView.setController(epoxyController)

        // Start loading shimmer
        showLoadingState()

        val folderId = args.folderId
        viewmodel.loadFiles(folderId)

        viewmodel.files.observe(viewLifecycleOwner) { response ->
            binding.swipeRefreshLayout.isRefreshing = false

            if (response.files?.isEmpty() ?: true) {
                showEmptyState()
            } else {
                epoxyController.files = response.files
                showDataState()
            }
        }

        binding.apply {

            folderName.text = args.folderName

            // Swipe to refresh listener
            swipeRefreshLayout.setOnRefreshListener {
                viewmodel.loadFiles(folderId)
            }


        }

        //File menu listener & controller
        epoxyController.clickListener = object : FileItemClickListener {
            override fun onMoreClick(fileId: String) {
                showFileOptionsBottomSheet(fileId)
            }
            override fun onFileClick(fileUrl: String, mimeType: String) {
                val action = FoldersFilesFragmentDirections
                    .actionFoldersFilesFragmentToFilePreviewFragment(fileUrl, mimeType)
                findNavController().navigate(action)
            }

            override fun onFolderClick(folderId: String, folderName: String) {
                TODO("Not yet implemented")
            }
        }

        // Delete file Listener
        viewmodel.deleteState.observe(viewLifecycleOwner) { event ->
            val deleted = event.getContentIfNotHandled() ?: return@observe

            if (deleted) {
                Toast.makeText(requireContext(), "File deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to delete file", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showFileOptionsBottomSheet(fileId: String) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_item_menu, null)

        val deleteBtn = view.findViewById<LinearLayout>(R.id.item_delete)
        val downloadBtn = view.findViewById<LinearLayout>(R.id.item_download)
        val shareBtn = view.findViewById<LinearLayout>(R.id.item_share)

        deleteBtn.setOnClickListener {
            viewmodel.deleteFile(fileId)
            dialog.dismiss()
        }

        downloadBtn.setOnClickListener {
            // viewmodel.downloadFile(fileId)
            dialog.dismiss()
        }

//        shareBtn.setOnClickListener {
//            viewmodel.generateShareLink(fileId)
//            dialog.dismiss()
//        }

        dialog.setContentView(view)
        dialog.show()
    }

    // Helper Functions
    private fun showLoadingState() {
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.shimmerLayout.startShimmer()
        binding.epoxyRecyclerView.visibility = View.GONE
        binding.noFilesTxt.visibility = View.GONE
    }

    private fun showDataState() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.epoxyRecyclerView.visibility = View.VISIBLE
        binding.noFilesTxt.visibility = View.GONE
    }

    private fun showEmptyState() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.epoxyRecyclerView.visibility = View.GONE
        binding.noFilesTxt.visibility = View.VISIBLE
    }


    // Detaching the binding to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}