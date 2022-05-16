package com.mk.competitors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.mk.base.SharedNotificationViewModel
import com.mk.base.UiEvent
import com.mk.base.hideKeyboard
import com.mk.competitors.databinding.CreateCompetitorFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCompetitorFragment : Fragment() {

    private lateinit var binding: CreateCompetitorFragmentBinding
    private val viewModel: CreateCompetitorViewModel by viewModels()
    private val sharedViewModel: SharedNotificationViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CreateCompetitorFragmentBinding.inflate(inflater, container, false)
        binding.button.setOnClickListener {
            viewModel.createCompetitor(binding.textInputLayout.editText?.text.toString())
        }

        viewModel.uiEvent.observe(viewLifecycleOwner) {
            hideKeyboard()
            when(it) {
                UiEvent.Success -> sharedViewModel.showNotification("User created")
                is UiEvent.Error -> sharedViewModel.showNotificationError(it.text)
            }
        }

        return binding.root
    }

}