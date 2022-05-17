package com.mk.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButtonToggleGroup
import com.mk.stats.databinding.FragmentRankBinding
import com.mk.stats.databinding.StatItemViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankFragment : Fragment() {

    private val viewModel: RankViewModel by viewModels()
    private lateinit var binding: FragmentRankBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRankBinding.inflate(inflater, container, false)

        val dataAdapter = Adapter()
        with(binding.recycler) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dataAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        binding.toggleButtonGroup.addOnButtonCheckedListener { _: MaterialButtonToggleGroup, checkedId: Int, isChecked: Boolean ->
            if (!isChecked) return@addOnButtonCheckedListener

            val sortType = when (checkedId) {
                R.id.first -> RankType.MatchesPlayed
                R.id.second -> RankType.MatchesWon
                else -> RankType.TotalScore
            }
            viewModel.changeRankType(sortType)
        }

        viewModel.rankType.observe(viewLifecycleOwner) {
            val id = when (it) {
                RankType.MatchesPlayed -> R.id.first
                RankType.MatchesWon -> R.id.second
                else -> R.id.third
            }

            binding.toggleButtonGroup.check(id)

            val starTitle = when (it) {
                RankType.MatchesPlayed -> R.string.games_played_title
                RankType.MatchesWon -> R.string.games_won_title
                else -> R.string.total_score_title
            }
            binding.statValue.text = getString(starTitle)
        }

        viewModel.data.observe(viewLifecycleOwner) {
            dataAdapter.submitNewData(it) {
                binding.recycler.scrollToPosition(0)
            }
        }
        return binding.root
    }

    class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {

        private val differ: AsyncListDiffer<PlayerStatUI> =
            AsyncListDiffer(this, DIFF_CALLBACK)

        companion object {
            val DIFF_CALLBACK = object : ItemCallback<PlayerStatUI>() {
                override fun areItemsTheSame(
                    oldItem: PlayerStatUI,
                    newItem: PlayerStatUI
                ): Boolean {
                    return oldItem.playerId == newItem.playerId
                }

                override fun areContentsTheSame(
                    oldItem: PlayerStatUI,
                    newItem: PlayerStatUI
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }

        class ViewHolder(private val binder: StatItemViewBinding) :
            RecyclerView.ViewHolder(binder.root) {

            fun bindData(data: PlayerStatUI) {
                with(binder) {
                    position.text = "#${data.position + 1}"
                    playerName.text = data.playerName
                    statValue.text = data.statValue.toString()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding =
                StatItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val data = differ.currentList[position]
            holder.bindData(data)
        }

        override fun getItemCount(): Int {
            return differ.currentList.size
        }

        fun submitNewData(dataSet: List<PlayerStatUI>, callback: Runnable?) {
            differ.submitList(dataSet, callback)
        }
    }
}