package com.mk.match

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mk.match.databinding.AddMatchFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMatchFragment : Fragment() {

    private val viewModel: AddMatchViewModel by viewModels()
    private lateinit var binding : AddMatchFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddMatchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPlayer1SelectionView()
        initPlayer2SelectionView()
        initSaveMatch()
    }

    fun initSaveMatch() {
        binding.button2.setOnClickListener {
            viewModel.saveMatchSTUB(binding.score1.text.toString(), binding.score2.text.toString())
        }
    }

    private fun initPlayer2SelectionView() {
        val player2Adapter = ArrayAdapter<UIPlayer>(
            requireContext(),
            android.R.layout.simple_list_item_1
        )
        (binding.spinner2.editText as? AutoCompleteTextView)?.
        apply {
            setAdapter(player2Adapter)
            onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    player2Adapter.getItem(position)?.let {
                        Log.d("!!!!", "P2 selected $it")
                        viewModel.player2Selected(it)
                    }
                }
        }

        viewModel.player2Suggestions.observe(viewLifecycleOwner) {
            player2Adapter.clear()
            player2Adapter.addAll(it)
        }

        viewModel.matchData.observe(viewLifecycleOwner, Observer {
            (binding.spinner2.editText as? AutoCompleteTextView)?.setText(it.player2.toString(), false)
        })
    }

    private fun initPlayer1SelectionView() {
        val player1Adapter = ArrayAdapter<UIPlayer>(requireContext(), android.R.layout.simple_list_item_1)
        (binding.spinner1.editText as? AutoCompleteTextView)?.apply {
            setAdapter(player1Adapter)
            onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    player1Adapter.getItem(position)?.let {
                        Log.d("!!!!", "P1 selected $it")
                        viewModel.player1Selected(it)
                    }
                }
        }

        viewModel.player1Suggestions.observe(viewLifecycleOwner, Observer {
            player1Adapter.clear()
            player1Adapter.addAll(it)
        })

        // this is a workaround for material component bug
        viewModel.matchData.observe(viewLifecycleOwner, Observer {
            (binding.spinner1.editText as? AutoCompleteTextView)?.setText(it.player1.toString(), false)
        })

//        binding.score1.addTextChangedListener { text: Editable? ->
//            viewModel.
//        }
    }
}