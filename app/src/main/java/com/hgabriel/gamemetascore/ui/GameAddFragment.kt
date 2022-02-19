package com.hgabriel.gamemetascore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hgabriel.gamemetascore.databinding.FragmentGameAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGameAddBinding.inflate(inflater, container, false)
        context ?: return binding.root
        return binding.root
    }

}
