package com.hgabriel.gamemetascore.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hgabriel.gamemetascore.R
import com.hgabriel.gamemetascore.data.IgdbGame
import com.hgabriel.gamemetascore.databinding.ListItemGameAddBinding
import com.hgabriel.gamemetascore.utilities.getTotalRatingTextColor
import com.hgabriel.gamemetascore.utilities.toLabel

class GameAddAdapter : ListAdapter<IgdbGame, RecyclerView.ViewHolder>(GameDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        GameAddViewHolder(
            ListItemGameAddBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val game = getItem(position)
        (holder as GameAddViewHolder).bind(game)
    }

    class GameAddViewHolder(private val binding: ListItemGameAddBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: IgdbGame) {
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

                // colors
                tvTotalRating.setTextColor(getTotalRatingTextColor(root.context, item.totalRating))

                // on click
                ivAdd.setOnClickListener { addGame(item, it) }

                executePendingBindings()
            }
        }

        private fun addGame(game: IgdbGame, view: View) {
            Toast.makeText(view.context, "SOON", Toast.LENGTH_SHORT).show()
            /*
            val direction =
                GamesFragmentDirections.actionGameListFragmentToGameDetailFragment(game.id)
            view.findNavController().navigate(direction)
             */
        }
    }

    private class GameDiffCallback : DiffUtil.ItemCallback<IgdbGame>() {
        override fun areItemsTheSame(oldItem: IgdbGame, newItem: IgdbGame): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: IgdbGame, newItem: IgdbGame): Boolean =
            oldItem == newItem
    }
}