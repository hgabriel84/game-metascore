package com.hgabriel.gamemetascore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hgabriel.gamemetascore.databinding.FragmentGameBinding
import com.hgabriel.gamemetascore.viewmodel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGameBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = GameAdapter()
        val linearLayoutManager = LinearLayoutManager(context)
        binding.rvGames.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
            this.adapter = adapter
        }
        subscribeUi(binding.tvEmptyState, adapter)

        return binding.root
    }

    private fun subscribeUi(emptyState: TextView, adapter: GameAdapter) {
        viewModel.games.observe(viewLifecycleOwner) { games ->
            emptyState.visibility = if (games.count() == 0) View.VISIBLE else View.GONE
            games.isEmpty()
            adapter.submitList(games)
        }
    }

}
