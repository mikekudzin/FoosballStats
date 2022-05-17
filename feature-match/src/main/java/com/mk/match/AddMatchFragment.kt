package com.mk.match

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mk.base.SharedNotificationViewModel
import com.mk.base.UiEvent
import com.mk.match.databinding.AddMatchFragmentBinding
import com.mk.match.di.AddMatchViewModelFactory
import com.mk.match.di.assistedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddMatchFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: AddMatchViewModelFactory

    private val args: AddMatchFragmentArgs by navArgs()
    private val viewModel: AddMatchViewModel by assistedViewModel {
        viewModelFactory.create(args.matchId)
    }

    private val sharedViewModel: SharedNotificationViewModel by activityViewModels()
    private lateinit var binding: AddMatchFragmentBinding

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

        viewModel.matchUI.observe(viewLifecycleOwner) { matchUI ->
            (binding.spinner1.editText as? AutoCompleteTextView)?.setText(
                matchUI.matchData.player1.toString(),
                false
            )
            (binding.spinner2.editText as? AutoCompleteTextView)?.setText(
                matchUI.matchData.player2.toString(),
                false
            )

            if (binding.score1.text.toString() != matchUI.matchData.score1) {
                binding.score1.setText(matchUI.matchData.score1)
            }

            if (binding.score2.text.toString() != matchUI.matchData.score2) {
                binding.score2.setText(matchUI.matchData.score2)
            }

            binding.button2.isEnabled = matchUI.dataValid

        }

        viewModel.uiEvent.observe(viewLifecycleOwner) {
            when (it) {
                UiEvent.Success -> {
                    sharedViewModel.showNotification("Match saved")
                    findNavController().popBackStack()
                }
                is UiEvent.Error -> {
                    sharedViewModel.showNotificationError(it.text)
                }
            }
        }

        initSaveMatch()
    }

    private fun initSaveMatch() {
        binding.button2.setOnClickListener {
            viewModel.saveMatch()
        }
    }

    private fun initPlayer2SelectionView() {
        val player2Adapter = ArrayAdapter<UIPlayer>(
            requireContext(),
            android.R.layout.simple_list_item_1
        )
        (binding.spinner2.editText as? AutoCompleteTextView)?.apply {
            setAdapter(player2Adapter)
            onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    player2Adapter.getItem(position)?.let {
                        viewModel.player2Selected(it)
                    }
                }
        }

        viewModel.player2Suggestions.observe(viewLifecycleOwner) {
            player2Adapter.clear()
            player2Adapter.addAll(it)
        }

        binding.score2.doAfterTextChanged { editable ->
            viewModel.player2ScoreSet(editable.toString())
        }
    }

    private fun initPlayer1SelectionView() {
        val player1Adapter =
            ArrayAdapter<UIPlayer>(requireContext(), android.R.layout.simple_list_item_1)
        (binding.spinner1.editText as? AutoCompleteTextView)?.apply {
            setAdapter(player1Adapter)
            onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    player1Adapter.getItem(position)?.let {
                        viewModel.player1Selected(it)
                    }
                }
        }

        viewModel.player1Suggestions.observe(viewLifecycleOwner) {
            player1Adapter.clear()
            player1Adapter.addAll(it)
        }

        binding.score1.doAfterTextChanged { editable ->
            viewModel.player1ScoreSet(editable.toString())
        }
    }
}