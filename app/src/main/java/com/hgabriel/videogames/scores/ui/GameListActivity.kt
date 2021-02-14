package com.hgabriel.videogames.scores.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hgabriel.videogames.scores.data.vo.Resource
import com.hgabriel.videogames.scores.databinding.ActivityGamelistBinding
import com.hgabriel.videogames.scores.viewmodel.GamesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameListActivity : AppCompatActivity() {

    private val viewModel by viewModels<GamesViewModel>()
    private lateinit var binding: ActivityGamelistBinding
    private lateinit var gameAdapter: GameAdapter

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
        }

    }

    private fun initGameList() {
        viewModel.games.observe(this, { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS ->
                    resource.data?.result?.let { gameAdapter.addAll(it) }
                Resource.Status.ERROR -> Unit
                Resource.Status.LOADING -> Unit
            }
        })
    }
}