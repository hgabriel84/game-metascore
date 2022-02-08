package com.hgabriel.gamemetascore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.hgabriel.gamemetascore.R
import com.hgabriel.gamemetascore.data.Game
import com.hgabriel.gamemetascore.databinding.FragmentGameDetailBinding
import com.hgabriel.gamemetascore.utilities.getTotalRatingTextColor
import com.hgabriel.gamemetascore.utilities.toLabel
import com.hgabriel.gamemetascore.viewmodels.GameDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameDetailFragment : Fragment() {

    private val viewModel: GameDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGameDetailBinding.inflate(inflater, container, false)
        context ?: return binding.root

        subscribeUi(binding)

        return binding.root
    }

    private fun subscribeUi(binding: FragmentGameDetailBinding) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is GameDetailViewModel.GameDetailUiState.Error -> setErrorState(binding)
                        GameDetailViewModel.GameDetailUiState.Loading -> setLoadingState(binding)
                        is GameDetailViewModel.GameDetailUiState.Success ->
                            setSuccessState(binding, uiState.game)
                    }
                }
            }
        }
    }

    private fun setErrorState(binding: FragmentGameDetailBinding) {
        binding.pbLoading.visibility = View.VISIBLE
    }

    private fun setLoadingState(binding: FragmentGameDetailBinding) {
        binding.pbLoading.visibility = View.VISIBLE
    }

    private fun setSuccessState(binding: FragmentGameDetailBinding, game: Game) {
        binding.apply {
            pbLoading.visibility = View.GONE

            // labels
            activity?.title = game.name
            tvSummary.text = game.summary
            tvTotalRating.text = game.totalRating.toLabel()
            tvCriticsRating.text = String.format(
                root.context.getString(R.string.critics_rating),
                game.criticsRating.toLabel()
            )
            tvUsersRating.text = String.format(
                root.context.getString(R.string.users_rating),
                game.usersRating.toLabel()
            )
            tvStoryline.text = game.storyline

            // checkbox
            cbLiked.isChecked = game.liked
            cbPlayed.isChecked = game.played

            // callbacks
            cbLiked.setOnClickListener { viewModel.toggleLiked() }
            cbPlayed.setOnClickListener { viewModel.togglePlayed() }
            clDelete.setOnClickListener {
                // prevent actions in game about to be deleted
                cbLiked.setOnClickListener(null)
                cbPlayed.setOnClickListener(null)
                onDelete(game)
            }

            // colors
            tvTotalRating.setTextColor(getTotalRatingTextColor(root.context, game.totalRating))

            // cover
            Glide.with(root).load(game.cover).into(ivCover)
        }
    }

    private fun onDelete(game: Game) {
        viewModel.delete()
        setFragmentResult(GAME_DETAIL_REQUEST_KEY, bundleOf(GAME_KEY to game))
        findNavController().navigateUp()
    }

    companion object {
        const val GAME_DETAIL_REQUEST_KEY = "GAME_DETAIL_REQUEST_KEY"
        const val GAME_KEY = "game"
    }

}
