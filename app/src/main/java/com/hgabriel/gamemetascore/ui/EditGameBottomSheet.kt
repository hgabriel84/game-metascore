package com.hgabriel.gamemetascore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hgabriel.gamemetascore.data.Game
import com.hgabriel.gamemetascore.databinding.BottomSheetEditGameBinding

class EditGameBottomSheet(
    private val game: Game,
    private val onPlayed: () -> Unit,
    private val onLiked: () -> Unit,
    private val onDelete: () -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetEditGameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetEditGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvName.text = game.name
        binding.cbPlayed.isChecked = game.played
        binding.cbPlayed.setOnCheckedChangeListener { _, _ ->
            onPlayed()
            this.dismiss()
        }
        binding.cbLiked.isChecked = game.liked
        binding.cbLiked.setOnCheckedChangeListener { _, _ ->
            onLiked()
            this.dismiss()
        }
        binding.clDelete.setOnClickListener {
            onDelete()
            this.dismiss()
        }
    }
}