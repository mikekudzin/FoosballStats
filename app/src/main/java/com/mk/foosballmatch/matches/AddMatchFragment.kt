package com.mk.foosballmatch.matches

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mk.foosballmatch.R

class AddMatchFragment : Fragment() {

    companion object {
        fun newInstance() = AddMatchFragment()
    }

    private lateinit var viewModel: AddMatchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_match_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[AddMatchViewModel::class.java]
        // TODO: Use the ViewModel
    }

}