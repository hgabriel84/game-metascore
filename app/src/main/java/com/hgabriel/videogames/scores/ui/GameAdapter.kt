package com.hgabriel.videogames.scores.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hgabriel.videogames.scores.R
import com.hgabriel.videogames.scores.data.vo.Game
import com.hgabriel.videogames.scores.databinding.ListItemGameBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class GameAdapter(private val list: ArrayList<Game>) :
    RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    class GameViewHolder(private val itemBinding: ListItemGameBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(game: Game) {
            val context = itemBinding.root.context
            // colors
            game.played?.takeIf { it }?.let {
                itemBinding.clContent.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.green_light)
                )
            } ?: itemBinding.clContent.setBackgroundColor(
                ContextCompat.getColor(context, R.color.grey_light)
            )
            itemBinding.tvAverageScore.setTextColor(
                getAverageScoreTextColor(context, game.averageScore)
            )

            // labels
            itemBinding.tvName.text = game.name
            itemBinding.tvMetascore.text =
                String.format(context.getString(R.string.metascore), game.metascore)
            itemBinding.tvUserscore.text =
                String.format(context.getString(R.string.userscore), game.userScore)
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            itemBinding.tvAverageScore.text = df.format(game.averageScore)

            // image
            Glide.with(itemBinding.root)
                .load(game.imageUrl)
                .into(itemBinding.ivCover)
        }

        private fun getAverageScoreTextColor(context: Context, score: Float) =
            when (score) {
                in 0f..49.9f -> ContextCompat.getColor(context, R.color.red_score)
                in 50f..79.9f -> ContextCompat.getColor(context, R.color.yellow_score)
                in 80f..100f -> ContextCompat.getColor(context, R.color.green_score)
                else -> ContextCompat.getColor(context, R.color.blue)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder =
        GameViewHolder(
            ListItemGameBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
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