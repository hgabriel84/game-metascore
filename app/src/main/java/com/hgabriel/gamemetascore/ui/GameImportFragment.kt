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
import com.google.android.material.snackbar.Snackbar
import com.hgabriel.gamemetascore.R
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
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        GameImportViewModel.GameImportUiState.Error -> setErrorState(binding)
                        GameImportViewModel.GameImportUiState.Initial -> setInitialState(binding)
                        GameImportViewModel.GameImportUiState.Loading -> setLoadingState(binding)
                        GameImportViewModel.GameImportUiState.Success -> setSuccessState(binding)
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

    private fun setErrorState(binding: FragmentGameImportBinding) {
        Snackbar
            .make(binding.root, getString(R.string.game_import_error), Snackbar.LENGTH_SHORT)
            .show()
        binding.apply {
            group.visibility = View.VISIBLE
            pbLoading.visibility = View.GONE
        }
    }

    private fun setSuccessState(binding: FragmentGameImportBinding) {
        Snackbar
            .make(binding.root, getString(R.string.game_import_error), Snackbar.LENGTH_SHORT)
            .show()
        binding.apply {
            group.visibility = View.VISIBLE
            pbLoading.visibility = View.GONE
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
    }

    private val fileResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val inputStream = activity?.contentResolver?.openInputStream(uri)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = bufferedReader.readText()
            adapter.fromJson(jsonString)?.let { viewModel.importGames(it) }
        }
}
