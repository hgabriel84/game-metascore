package com.hgabriel.videogames.scores.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hgabriel.videogames.scores.data.vo.Game
import com.hgabriel.videogames.scores.databinding.ListItemGameBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class GameAdapter(private val list: ArrayList<Game>) :
    RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    class GameViewHolder(private val itemBinding: ListItemGameBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(game: Game) {
            itemBinding.tvName.text = game.name
            itemBinding.tvMetascore.text = "metascore: ${game.metascore}"
            itemBinding.tvUserscore.text = "user score: ${game.userScore}"
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            itemBinding.tvAverageScore.text = df.format(game.averageScore)
            Glide.with(itemBinding.root)
                .load(game.imageUrl)
                .into(itemBinding.ivCover)
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