package com.hgabriel.videogames.scores.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hgabriel.videogames.scores.R
import com.hgabriel.videogames.scores.data.vo.Resource
import com.hgabriel.videogames.scores.databinding.ActivityGamelistBinding
import com.hgabriel.videogames.scores.viewmodel.GamesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GameListActivity : AppCompatActivity() {

    private val viewModel by viewModels<GamesViewModel>()
    private lateinit var binding: ActivityGamelistBinding
    private lateinit var gameAdapter: GameAdapter
    private lateinit var loadigSnackbar: Snackbar
    private lateinit var errorSnackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamelistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLayout()
        initGameList()
    }

    private fun setupLayout() {
        gameAdapter = GameAdapter(arrayListOf())
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rvGames.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(this.context, linearLayoutManager.orientation))
            adapter = gameAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        binding.fab.hide()
                    } else {
                        binding.fab.show()
                    }
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }
        loadigSnackbar = Snackbar
            .make(
                binding.coordinatorLayout,
                R.string.updating_data_label,
                Snackbar.LENGTH_INDEFINITE
            )
            .setBackgroundTint(ContextCompat.getColor(this, R.color.blue))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
        errorSnackbar =
            Snackbar.make(binding.coordinatorLayout, R.string.error, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(this, R.color.red_score))
                .setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.fab.setOnClickListener {
            Toast.makeText(this, "ADD GAME", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initGameList() {
        viewModel.games.observe(this, { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    loadigSnackbar.dismiss()
                    errorSnackbar.dismiss()
                    resource.data?.result?.let { gameAdapter.addAll(it) }
                }
                Resource.Status.ERROR -> errorSnackbar.show()
                Resource.Status.LOADING -> loadigSnackbar.show()
            }
        })
    }
}