package com.mk.match

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.mk.match.databinding.MatchHistoryItemBinding
import com.mk.match.databinding.MatchesHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchesHistoryFragment : Fragment() {
    private val viewModel: MatchesHistoryViewModel by viewModels()
    private lateinit var binding: MatchesHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MatchesHistoryBinding.inflate(inflater, container, false)
        binding.addMatch.setOnClickListener {
            findNavController().navigate(R.id.addMatchFragment)
        }

        val dataAdapter = Adapter(onEditMatch = { matchId ->
            val action =
                MatchesHistoryFragmentDirections.actionMatchesHistoryFragmentToAddMatchFragment(
                    matchId
                )
            findNavController().navigate(action)
        },
            onDeleteMatch = { matchId ->
                viewModel.deleteMatch(matchId)
            }
        )

        with(binding.recycler) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dataAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.HORIZONTAL
                )
            )
        }

        viewModel.matches.observe(viewLifecycleOwner) {
            dataAdapter.submitNewData(it) {
                binding.recycler.scrollToPosition(0)
            }
        }

        return binding.root
    }

    class Adapter(
        val onEditMatch: ((matchId: Int) -> Unit),
        val onDeleteMatch: ((matchId: Int) -> Unit)
    ) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        private val differ: AsyncListDiffer<MatchesHistoryViewModel.MatchStats> =
            AsyncListDiffer(this, DIFF_CALLBACK)

        companion object {
            val DIFF_CALLBACK =
                object : DiffUtil.ItemCallback<MatchesHistoryViewModel.MatchStats>() {
                    override fun areItemsTheSame(
                        oldItem: MatchesHistoryViewModel.MatchStats,
                        newItem: MatchesHistoryViewModel.MatchStats
                    ): Boolean {
                        return oldItem.id == newItem.id
                    }

                    override fun areContentsTheSame(
                        oldItem: MatchesHistoryViewModel.MatchStats,
                        newItem: MatchesHistoryViewModel.MatchStats
                    ): Boolean {
                        return oldItem == newItem
                    }
                }
        }

        inner class ViewHolder(private val binder: MatchHistoryItemBinding) :
            RecyclerView.ViewHolder(binder.root), View.OnCreateContextMenuListener {

            init {
                binder.root.setOnCreateContextMenuListener(this)
            }

            fun bindData(data: MatchesHistoryViewModel.MatchStats) {
                with(binder) {
                    date.text = data.date
                    players.text =
                        "${data.player1Name} ${data.player1Score} : ${data.player2Score} ${data.player2Name}"
                }
            }

            override fun onCreateContextMenu(
                menu: ContextMenu,
                v: View?,
                menuInfo: ContextMenu.ContextMenuInfo?
            ) {
                val editMatch: MenuItem = menu.add(Menu.NONE, 1, 1, "Edit")
                val deleteMatch: MenuItem = menu.add(Menu.NONE, 2, 2, "Delete")

                editMatch.setOnMenuItemClickListener {
                    val matchId = differ.currentList[adapterPosition].id
                    onEditMatch(matchId)
                    true
                }

                deleteMatch.setOnMenuItemClickListener {
                    val matchId = differ.currentList[adapterPosition].id
                    onDeleteMatch(matchId)
                    true
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding =
                MatchHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val data = differ.currentList[position]
            holder.bindData(data)
        }

        override fun getItemCount(): Int {
            return differ.currentList.size
        }

        fun submitNewData(dataSet: List<MatchesHistoryViewModel.MatchStats>, callback: Runnable?) {
            differ.submitList(dataSet, callback)
        }
    }
}