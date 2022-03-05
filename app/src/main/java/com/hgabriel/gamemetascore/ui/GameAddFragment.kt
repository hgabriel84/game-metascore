package com.hgabriel.gamemetascore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hgabriel.gamemetascore.adapters.GameAddAdapter
import com.hgabriel.gamemetascore.data.IgdbGame
import com.hgabriel.gamemetascore.databinding.FragmentGameAddBinding
import com.hgabriel.gamemetascore.viewmodels.GameAddViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameAddFragment : Fragment() {

    private val viewModel: GameAddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGameAddBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = GameAddAdapter { addGame(it) }
        val linearLayoutManager = LinearLayoutManager(context)
        binding.rvGames.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
            this.adapter = adapter
        }

        setupToolbar(binding)
        subscribeUi(binding, adapter)

        return binding.root
    }

    private fun setupToolbar(binding: FragmentGameAddBinding) {
        binding.svSearch.setOnQueryTextListener(
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
    }

    private fun subscribeUi(binding: FragmentGameAddBinding, adapter: GameAddAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        GameAddViewModel.GameAddUiState.Loading -> setLoadingState(binding)
                        GameAddViewModel.GameAddUiState.Empty -> setEmptyState(binding)
                        is GameAddViewModel.GameAddUiState.Error ->
                            setErrorState(binding, uiState.message)
                        is GameAddViewModel.GameAddUiState.Success ->
                            setSuccessState(binding, uiState.games, adapter)
                    }
                }
            }
        }
    }

    private fun setLoadingState(binding: FragmentGameAddBinding) {
        binding.apply {
            pbLoading.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE
            tvNoResults.visibility = View.GONE
            rvGames.visibility = View.GONE
        }
    }

    private fun setEmptyState(binding: FragmentGameAddBinding) {
        binding.apply {
            tvEmpty.visibility = View.VISIBLE
            tvNoResults.visibility = View.GONE
            pbLoading.visibility = View.GONE
            rvGames.visibility = View.GONE
        }
    }

    private fun setErrorState(binding: FragmentGameAddBinding, message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        binding.apply {
            tvNoResults.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE
            pbLoading.visibility = View.GONE
            rvGames.visibility = View.GONE
        }
    }

    private fun setSuccessState(
        binding: FragmentGameAddBinding,
        games: List<IgdbGame>,
        adapter: GameAddAdapter
    ) {
        binding.apply {
            pbLoading.visibility = View.GONE
            tvEmpty.visibility = View.GONE
        }
        if (games.isEmpty()) {
            binding.apply {
                tvNoResults.visibility = View.VISIBLE
                rvGames.visibility = View.GONE
            }
        } else {
            adapter.submitList(games)
            binding.apply {
                tvNoResults.visibility = View.GONE
                rvGames.visibility = View.VISIBLE
            }
        }
    }

    private fun addGame(igdbGame: IgdbGame) {
        viewModel.addGame(igdbGame)
    }
}
