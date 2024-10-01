package com.presentation.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.domain.entities.ShowDataStatus
import com.google.android.material.snackbar.Snackbar
import com.presentation.R
import com.presentation.databinding.FragmentListContactsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListContactsFragment : Fragment(R.layout.fragment_list_contacts) {

    private var _binding: FragmentListContactsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListContactsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadContacts()
        loadingProcess()
        refreshSwipe()
    }

    private fun refreshSwipe() {
        binding.swipeLayout.setOnRefreshListener {
            viewModel.refreshList()
            binding.cloudImage.visibility = View.GONE
            binding.swipeLayout.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            binding.swipeLayout.isRefreshing = false
        }
    }

    private fun loadingProcess() {
        binding.swipeLayout.visibility = View.GONE
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getShowDataStatus().collectLatest { showDataStatus ->
                    when (showDataStatus) {
                        ShowDataStatus.Show -> {
                            binding.progressBar.visibility = View.GONE
                            binding.cloudImage.visibility = View.GONE
                            binding.swipeLayout.visibility = View.VISIBLE
                        }

                        ShowDataStatus.NetworkErrorEmptyDatabase -> {
                            binding.progressBar.visibility = View.GONE
                            binding.swipeLayout.visibility = View.VISIBLE
                            binding.cloudImage.visibility = View.VISIBLE
                            Snackbar.make(
                                requireContext(),
                                binding.root,
                                getString(R.string.network_error),
                                Snackbar.LENGTH_LONG
                            )
                                .show()
                        }

                        ShowDataStatus.NetworkError -> {
                            binding.swipeLayout.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            binding.cloudImage.visibility = View.GONE
                            Snackbar.make(
                                requireContext(),
                                binding.root,
                                getString(R.string.network_error),
                                Snackbar.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun loadContacts() {
        binding.recyclerView.adapter = PagingAdapter(
            onItemClicked = {
                val action =
                    ListContactsFragmentDirections.actionListContactsFragmentToInfoContactFragment(
                        idContact = it.id
                    )
                findNavController().navigate(action)
            }
        )
        subscribeRecycler()
    }

    private fun subscribeRecycler() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getContactsList().collectLatest { pagerData ->
                    (binding.recyclerView.adapter as PagingAdapter).submitData(pagerData)
                }
            }
        }
    }

}