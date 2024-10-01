package com.presentation.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.presentation.R
import com.presentation.databinding.FragmentInfoContactBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InfoContactFragment : Fragment(R.layout.fragment_info_contact) {

    private var _binding: FragmentInfoContactBinding? = null
    private val binding get() = _binding!!
    private val infoContactViewModel: InfoContactViewModel by viewModels()
    private val args: InfoContactFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayContact()
        callDialer()
    }

    private fun displayContact() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                infoContactViewModel.getOneContactById(args.idContact).collect { contact ->
                    binding.name.text = contact.name
                    binding.phone.text = contact.phone
                    binding.temperament.text = contact.temperament.name
                    binding.educationPeriod.text = contact.educationPeriod
                    binding.biography.text = contact.biography
                }
            }
        }
    }

    private fun callDialer() {
        binding.phone.setOnClickListener {
            val phone: Uri = Uri.parse("tel:" + binding.phone.text)
            val dial = Intent(Intent.ACTION_DIAL, phone)
            startActivity(dial)
        }
    }

}