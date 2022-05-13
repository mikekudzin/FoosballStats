package com.mk.foosballmatch.competitors

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mk.foosballmatch.R
import com.mk.foosballmatch.databinding.CreateCompetitorFragmentBinding
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