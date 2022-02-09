package com.hgabriel.gamemetascore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hgabriel.gamemetascore.databinding.FragmentGameSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameSearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGameSearchBinding.inflate(inflater, container, false)
        context ?: return binding.root
        return binding.root
    }

}