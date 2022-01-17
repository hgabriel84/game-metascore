package com.hgabriel.gamemetascore.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hgabriel.gamemetascore.R
import com.hgabriel.gamemetascore.data.Game
import com.hgabriel.gamemetascore.databinding.ListItemGameBinding
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class GameAdapter(
    private val list: ArrayList<Game>,
    private val onLongClickCallback: (game: Game) -> Unit
) :
    RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    class GameViewHolder(
        private val itemBinding: ListItemGameBinding,
        private val onLongClickCallback: (game: Game) -> Unit
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(game: Game) {
            val context = itemBinding.root.context
            // colors
            if (game.played) {
                itemBinding.clContent.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.list_item_game_played_bg
                    )
                )
            } else {
                itemBinding.clContent.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.list_item_game_bg
                    )
                )
            }
            itemBinding.tvTotalRating.setTextColor(
                getTotalRatingTextColor(
                    context,
                    game.totalRating
                )
            )

            // labels
            itemBinding.tvName.text = game.name
            itemBinding.tvCriticsRating.text =
                String.format(
                    context.getString(R.string.critics_rating),
                    game.criticsRating.toLabel()
                )
            itemBinding.tvUsersRating.text =
                String.format(context.getString(R.string.users_rating), game.usersRating.toLabel())
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            itemBinding.tvTotalRating.text = game.totalRating.toLabel()

            // image
            Glide.with(itemBinding.root).load(game.cover).into(itemBinding.ivCover)

            // liked game
            itemBinding.ivLiked.visibility = if (game.liked) View.VISIBLE else View.INVISIBLE

            // long click
            itemView.setOnLongClickListener {
                onLongClickCallback(game)
                true
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder =
        GameViewHolder(
            ListItemGameBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onLongClickCallback
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addAll(games: List<Game>) {
        list.apply {
            clear()
            addAll(games)
        }
        notifyDataSetChanged()
    }
}
