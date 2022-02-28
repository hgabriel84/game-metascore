package com.hgabriel.gamemetascore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hgabriel.gamemetascore.data.MetaGames
import com.hgabriel.gamemetascore.databinding.FragmentGameImportBinding
import com.hgabriel.gamemetascore.viewmodels.GameImportViewModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

@AndroidEntryPoint
class GameImportFragment : Fragment() {

    private val viewModel: GameImportViewModel by viewModels()
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val adapter: JsonAdapter<MetaGames> = moshi.adapter(MetaGames::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGameImportBinding.inflate(inflater, container, false)
        context ?: return binding.root

        setupListeners(binding)
        subscribeUi(binding)

        return binding.root
    }

    private fun subscribeUi(binding: FragmentGameImportBinding) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.importUiState.collect { uiState ->
                    when (uiState) {
                        GameImportViewModel.GameImportUiState.Initial -> setInitialState(binding)
                        GameImportViewModel.GameImportUiState.Loading -> setLoadingState(binding)
                    }
                }
            }
        }
    }

    private fun setLoadingState(binding: FragmentGameImportBinding) {
        binding.apply {
            pbLoading.visibility = View.VISIBLE
            group.visibility = View.GONE
        }
    }

    private fun setInitialState(binding: FragmentGameImportBinding) {
        binding.apply {
            group.visibility = View.VISIBLE
            pbLoading.visibility = View.GONE
        }
    }

    private fun setupListeners(binding: FragmentGameImportBinding) {
        binding.btImportFile.setOnClickListener { fileResult.launch("application/json") }
        binding.btExportFile.setOnClickListener { saveResult.launch("games.json") }
    }

    private val fileResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val inputStream = activity?.contentResolver?.openInputStream(uri)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = bufferedReader.readText()
            adapter.fromJson(jsonString)?.let { viewModel.importGames(it) }
        }

    private val saveResult =
        registerForActivityResult(ActivityResultContracts.CreateDocument()) { uri ->
            val json = adapter.toJson(viewModel.exportGames)
            val writer = activity?.contentResolver?.openOutputStream(uri)?.writer()
            writer?.apply {
                write(json)
                close()
            }
        }
}
