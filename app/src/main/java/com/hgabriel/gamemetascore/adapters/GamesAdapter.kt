package com.hgabriel.gamemetascore.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hgabriel.gamemetascore.R
import com.hgabriel.gamemetascore.data.Game
import com.hgabriel.gamemetascore.databinding.ListItemGameBinding
import com.hgabriel.gamemetascore.ui.GamesFragmentDirections
import com.hgabriel.gamemetascore.utilities.getTotalRatingTextColor
import com.hgabriel.gamemetascore.utilities.toLabel

class GamesAdapter : ListAdapter<Game, RecyclerView.ViewHolder>(GameDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        GameViewHolder(
            ListItemGameBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val game = getItem(position)
        (holder as GameViewHolder).bind(game)
    }

    class GameViewHolder(private val binding: ListItemGameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Game) {
            binding.apply {
                // labels
                tvName.text = item.name
                tvCriticsRating.text = String.format(
                    root.context.getString(R.string.critics_rating),
                    item.criticsRating.toLabel()
                )
                tvUsersRating.text = String.format(
                    root.context.getString(R.string.users_rating),
                    item.usersRating.toLabel()
                )
                tvTotalRating.text = item.totalRating.toLabel()

                // image
                Glide.with(root).load(item.cover).into(ivCover)

                // liked game
                ivLiked.visibility = if (item.liked) View.VISIBLE else View.INVISIBLE

                // played game
                ivPlayed.visibility = if (item.played) View.VISIBLE else View.INVISIBLE

                // on click
                itemView.setOnClickListener {
                    navigateToGame(item, it)
                }

                // colors
                tvTotalRating.setTextColor(getTotalRatingTextColor(root.context, item.totalRating))

                executePendingBindings()
            }
        }

        private fun navigateToGame(game: Game, view: View) {
            val direction =
                GamesFragmentDirections.actionGameListFragmentToGameDetailFragment(game.id)
            view.findNavController().navigate(direction)
        }
    }

    private class GameDiffCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean = oldItem == newItem
    }
}
