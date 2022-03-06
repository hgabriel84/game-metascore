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
import com.hgabriel.gamemetascore.R
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
                        is GameAddViewModel.GameAddUiState.Error ->
                            setErrorState(binding, uiState.message)
                        is GameAddViewModel.GameAddUiState.Success ->
                            setSuccessState(binding, uiState.games, adapter)
                        GameAddViewModel.GameAddUiState.Loading -> setLoadingState(binding)
                        GameAddViewModel.GameAddUiState.Initial -> setInitialState(binding)
                        GameAddViewModel.GameAddUiState.NoResults -> setNoResultsState(binding)
                    }
                }
            }
        }
    }

    private fun setLoadingState(binding: FragmentGameAddBinding) {
        setState(
            binding = binding,
            toolbarVisibility = View.GONE,
            recyclerViewVisibility = View.GONE,
            emptyStateVisibility = View.GONE,
            loadingVisibility = View.VISIBLE
        )
    }

    private fun setInitialState(binding: FragmentGameAddBinding) {
        binding.tvEmptyState.text = getString(R.string.add_game_initial)
        setState(
            binding = binding,
            toolbarVisibility = View.VISIBLE,
            recyclerViewVisibility = View.GONE,
            emptyStateVisibility = View.VISIBLE,
            loadingVisibility = View.GONE
        )
    }

    private fun setNoResultsState(binding: FragmentGameAddBinding) {
        binding.tvEmptyState.text = getString(R.string.no_game_found)
        setState(
            binding = binding,
            toolbarVisibility = View.VISIBLE,
            recyclerViewVisibility = View.GONE,
            emptyStateVisibility = View.VISIBLE,
            loadingVisibility = View.GONE
        )
    }

    private fun setErrorState(binding: FragmentGameAddBinding, message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        setNoResultsState(binding)
    }

    private fun setSuccessState(
        binding: FragmentGameAddBinding,
        games: List<IgdbGame>,
        adapter: GameAddAdapter
    ) {
        adapter.submitList(games)
        setState(
            binding = binding,
            toolbarVisibility = View.VISIBLE,
            recyclerViewVisibility = View.VISIBLE,
            emptyStateVisibility = View.GONE,
            loadingVisibility = View.GONE
        )
    }

    private fun setState(
        binding: FragmentGameAddBinding,
        toolbarVisibility: Int,
        recyclerViewVisibility: Int,
        emptyStateVisibility: Int,
        loadingVisibility: Int
    ) {
        binding.apply {
            toolbar.visibility = toolbarVisibility
            rvGames.visibility = recyclerViewVisibility
            tvEmptyState.visibility = emptyStateVisibility
            pbLoading.visibility = loadingVisibility
        }
    }

    private fun addGame(igdbGame: IgdbGame) {
        viewModel.addGame(igdbGame)
    }
}
