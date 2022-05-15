package com.mk.competitors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mk.competitors.databinding.CreateCompetitorFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCompetitorFragment : Fragment() {

    companion object {
        fun newInstance() = CreateCompetitorFragment()
    }

    private lateinit var binding: CreateCompetitorFragmentBinding
    private val viewModel: CreateCompetitorViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateCompetitorFragmentBinding.inflate(inflater, container, false)
        binding.button.setOnClickListener {
            viewModel.createCompetitor(binding.textInputLayout.editText?.text.toString())
        }
        return binding.root
    }


}