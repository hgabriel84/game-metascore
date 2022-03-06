package com.hgabriel.gamemetascore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hgabriel.gamemetascore.R
import com.hgabriel.gamemetascore.adapters.GamesAdapter
import com.hgabriel.gamemetascore.data.Game
import com.hgabriel.gamemetascore.databinding.FragmentGamesBinding
import com.hgabriel.gamemetascore.viewmodels.GamesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GamesFragment : Fragment() {

    private val viewModel: GamesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGamesBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = GamesAdapter()
        val linearLayoutManager = LinearLayoutManager(context)
        binding.rvGames.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
            this.adapter = adapter
        }
        subscribeUi(binding, adapter)

        setFragmentResultListener(GameDetailFragment.GAME_DETAIL_REQUEST_KEY) { _, bundle ->
            val game = (bundle.get(GameDetailFragment.GAME_KEY) as Game)
            setRestoreGameState(binding.root, game)
        }

        return binding.root
    }

    private fun subscribeUi(binding: FragmentGamesBinding, adapter: GamesAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is GamesViewModel.GamesUiState.Success ->
                            setSuccessState(binding, uiState.games, uiState.orderIcon, adapter)
                        GamesViewModel.GamesUiState.Loading -> setLoadingState(binding)
                        GamesViewModel.GamesUiState.Empty -> setEmptyState(binding)
                        GamesViewModel.GamesUiState.NoResults -> setNoResultsState(binding)
                    }
                }
            }
        }
    }

    private fun setLoadingState(binding: FragmentGamesBinding) {
        binding.apply {
            tvEmptyState.visibility = View.GONE
            grGames.visibility = View.GONE
            pbLoading.visibility = View.VISIBLE
        }
    }

    private fun setEmptyState(binding: FragmentGamesBinding) {
        binding.apply {
            grGames.visibility = View.GONE
            pbLoading.visibility = View.GONE
            tvEmptyState.text = getString(R.string.add_game_explained)
            tvEmptyState.visibility = View.VISIBLE
        }
    }

    private fun setNoResultsState(binding: FragmentGamesBinding) {
        binding.apply {
            grGames.visibility = View.GONE
            pbLoading.visibility = View.GONE
            tvEmptyState.text = getString(R.string.no_game_found)
            tvEmptyState.visibility = View.VISIBLE
            svGame.visibility = View.VISIBLE
        }
    }

    private fun setSuccessState(
        binding: FragmentGamesBinding,
        games: List<Game>,
        orderIcon: Int,
        adapter: GamesAdapter
    ) {
        binding.apply {
            pbLoading.visibility = View.GONE
            tvEmptyState.visibility = View.GONE
            ivSort.apply {
                setImageResource(orderIcon)
                setOnClickListener { viewModel.toggleOrder() }
            }
            svGame.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        viewModel.search(query)
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        if (newText.isEmpty()) viewModel.search("")
                        return false
                    }
                }
            )
            grGames.visibility = View.VISIBLE
        }
        adapter.submitList(games)
    }

    private fun setRestoreGameState(view: View, game: Game) {
        Snackbar.make(view, R.string.game_deleted, Snackbar.LENGTH_SHORT)
            .setAction(R.string.undo) { viewModel.restoreGame(game) }
            .show()
    }

}
