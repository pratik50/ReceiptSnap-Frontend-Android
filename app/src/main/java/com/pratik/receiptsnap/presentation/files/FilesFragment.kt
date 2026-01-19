package com.pratik.receiptsnap.presentation.files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pratik.receiptsnap.R
import com.pratik.receiptsnap.databinding.FragmentFilesBinding
import com.pratik.receiptsnap.model.FileItem
import com.pratik.receiptsnap.model.FolderItem
import com.pratik.receiptsnap.presentation.organize.OrganizeFragmentDirections
import com.pratik.receiptsnap.presentation.files.state.FileItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class FilesFragment : Fragment() {

    private var _binding: FragmentFilesBinding?= null
    private val binding get() = _binding!!
    private val viewmodel: FilesViewModel by viewModels()
    private lateinit var epoxyController: FilesEpoxyController
    private var allFiles: List<FileItem> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Epoxy Initialization
        epoxyController = FilesEpoxyController()
        binding.epoxyRecyclerView.setController(epoxyController)

        // Search list
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRecyclerView.setController(epoxyController)

        // Link SearchBar + SearchView
        binding.searchView.setupWithSearchBar(binding.searchBar)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.searchView.isShowing) {
                binding.searchView.hide()
            } else {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }

        // Start loading shimmer
        showLoadingState()
        viewmodel.loadFiles()

        viewmodel.files.observe(viewLifecycleOwner) { response ->
            binding.swipeRefreshLayout.isRefreshing = false

            if (response.files?.isEmpty() ?: true) {
                showEmptyState()
            } else {
                epoxyController.files = response.files
                allFiles = response.files
                showDataState()
            }
        }

        binding.apply {

//            // Open Navigation drawer
//            searchBar.setNavigationOnClickListener{ view->
//                drawerLayout.open()
//            }

            // Swipe to refresh listener
            swipeRefreshLayout.setOnRefreshListener {
                viewmodel.loadFiles()
            }

            // Navigation drawer item listener
//            navigationView.setNavigationItemSelectedListener { menuItem->
//
//                menuItem.isChecked = true
//
//                when (menuItem.itemId){
//                    R.id.action_recent -> {
//                        drawerLayout.close()
//                        Toast.makeText(requireContext(), "Recent", Toast.LENGTH_SHORT).show()
//                        true
//                    }
//                    R.id.action_offline -> {
//                        drawerLayout.close()
//                        Toast.makeText(requireContext(), "Offline", Toast.LENGTH_SHORT).show()
//                        true
//                    }
//                    R.id.action_trash -> {
//                        drawerLayout.close()
//                        Toast.makeText(requireContext(), "Bin", Toast.LENGTH_SHORT).show()
//                        true
//                    }
//                    R.id.action_settings -> {
//                        drawerLayout.close()
//                        Toast.makeText(requireContext(), "Settings", Toast.LENGTH_SHORT).show()
//                        true
//                    }
//                    R.id.action_help -> {
//                        drawerLayout.close()
//                        Toast.makeText(requireContext(), "Help", Toast.LENGTH_SHORT).show()
//                        true
//                    }
//                    R.id.action_logout -> {
//                        drawerLayout.close()
//                        viewmodel.logout(userPrefs)
//
//                        val navOptions = NavOptions.Builder()
//                            .setPopUpTo(R.id.organizeFragment, true)
//                            .build()
//                        findNavController().navigate(R.id.action_organizeFragment_to_loginFragment, null, navOptions)
//
//                        Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                menuItem.isChecked = false
//                return@setNavigationItemSelectedListener true
//            }

            searchView.addTransitionListener { _, _, newState ->
                val bottomNav = requireActivity().findViewById<View>(R.id.bottom_Bar) // id check kar!

                when (newState) {
                    com.google.android.material.search.SearchView.TransitionState.SHOWING,
                    com.google.android.material.search.SearchView.TransitionState.SHOWN -> {
                        bottomNav.visibility = View.GONE
                    }
                    com.google.android.material.search.SearchView.TransitionState.HIDING,
                    com.google.android.material.search.SearchView.TransitionState.HIDDEN -> {
                        bottomNav.visibility = View.VISIBLE
                        epoxyController.files = allFiles // restore folders
                    }
                }
            }

            searchView.editText.setOnEditorActionListener { v, _, _ ->
                val query = v.text.toString()
                filterList(query)
                false
            }

            searchView.editText.addTextChangedListener { text ->
                filterList(text.toString())
            }

        }


        //File menu listener & controller
        epoxyController.clickListener = object : FileItemClickListener {
            override fun onMoreClick(fileId: String) {
                showFileOptionsBottomSheet(fileId)
            }
            override fun onFileClick(fileUrl: String, mimeType: String) {
                val action = FilesFragmentDirections
                    .actionFilesFragmentToFilePreviewFragment(fileUrl, mimeType)
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

    private fun filterList(query: String) {
        val filteredList = if (query.isEmpty()) {
            emptyList<FileItem>()
        } else {
            allFiles.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
        epoxyController.files = filteredList
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