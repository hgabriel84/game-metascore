package com.hgabriel.gamemetascore.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hgabriel.gamemetascore.R
import com.hgabriel.gamemetascore.data.Game
import com.hgabriel.gamemetascore.databinding.ListItemGameBinding
import kotlin.math.roundToInt

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

                // long click
                itemView.setOnLongClickListener {
                    Toast.makeText(root.context, "SOON", Toast.LENGTH_SHORT).show()
                    true
                }

                // colors
                if (item.played) {
                    clContent.setBackgroundColor(
                        ContextCompat.getColor(root.context, R.color.list_item_game_played_bg)
                    )
                } else {
                    clContent.setBackgroundColor(
                        ContextCompat.getColor(root.context, R.color.list_item_game_bg)
                    )
                }
                tvTotalRating.setTextColor(
                    getTotalRatingTextColor(root.context, item.totalRating)
                )

                executePendingBindings()
            }
        }

        private fun Float?.toLabel(): String = this?.roundToInt()?.toString() ?: "-"

        private fun getTotalRatingTextColor(context: Context, score: Float?) =
            score?.let {
                when (it) {
                    in 0f..49.9f -> ContextCompat.getColor(context, R.color.red_score)
                    in 50f..79.9f -> ContextCompat.getColor(context, R.color.yellow_score)
                    in 80f..100f -> ContextCompat.getColor(context, R.color.green_score)
                    else -> ContextCompat.getColor(context, R.color.primary)
                }
            } ?: ContextCompat.getColor(context, R.color.primary)
    }
}

private class GameDiffCallback : DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean = oldItem == newItem

}

/*
class GameAdapter(
    private val list: ArrayList<Game>,
    private val onLongClickCallback: (game: Game) -> Unit
)
*/