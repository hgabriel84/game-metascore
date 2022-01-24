package com.hgabriel.gamemetascore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hgabriel.gamemetascore.R
import com.hgabriel.gamemetascore.data.Game
import com.hgabriel.gamemetascore.data.GameOrder
import com.hgabriel.gamemetascore.databinding.FragmentGameBinding
import com.hgabriel.gamemetascore.viewmodel.GamesViewModel
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
        val binding = FragmentGameBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = GamesAdapter()
        val linearLayoutManager = LinearLayoutManager(context)
        binding.rvGames.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
            this.adapter = adapter
        }
        subscribeUi(binding, adapter)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.game_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_order)?.let { item ->
            viewModel.getOrderIcon().also { item.setIcon(it) }
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_order -> {
                viewModel.toggleOrder()
                activity?.invalidateOptionsMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeUi(binding: FragmentGameBinding, adapter: GamesAdapter) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is GamesViewModel.GamesUiState.Error -> setErrorState(binding)
                        GamesViewModel.GamesUiState.Loading -> setLoadingState(binding)
                        is GamesViewModel.GamesUiState.Success ->
                            setSuccessState(binding, uiState.games, adapter)
                    }
                }
            }
        }
    }

    private fun setLoadingState(binding: FragmentGameBinding) {
        binding.tvEmptyState.visibility = View.GONE
        binding.pbLoading.visibility = View.VISIBLE
        binding.rvGames.visibility = View.GONE
    }

    private fun setErrorState(binding: FragmentGameBinding) {

    }

    private fun setSuccessState(
        binding: FragmentGameBinding,
        games: List<Game>,
        adapter: GamesAdapter
    ) {
        binding.pbLoading.visibility = View.GONE
        if (games.isEmpty()) {
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.rvGames.visibility = View.GONE
        } else {
            binding.tvEmptyState.visibility = View.GONE
            binding.rvGames.visibility = View.VISIBLE
            adapter.submitList(games)
        }
    }

}
