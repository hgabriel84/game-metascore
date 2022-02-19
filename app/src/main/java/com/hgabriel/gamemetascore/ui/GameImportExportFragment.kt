package com.hgabriel.gamemetascore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hgabriel.gamemetascore.databinding.FragmentGameImportExportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameImportExportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGameImportExportBinding.inflate(inflater, container, false)
        context ?: return binding.root
        return binding.root
    }

}
