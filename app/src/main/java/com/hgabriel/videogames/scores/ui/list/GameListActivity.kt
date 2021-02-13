package com.hgabriel.videogames.scores.ui.list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.hgabriel.videogames.scores.R
import com.hgabriel.videogames.scores.data.vo.Resource
import com.hgabriel.videogames.scores.viewmodel.GamesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameListActivity : AppCompatActivity() {

    private val viewModel by viewModels<GamesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gamelist)

        setupLayout()
        initGameList()
    }

    private fun setupLayout() {

    }

    private fun initGameList() {
        viewModel.games.observe(this, Observer { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> Unit
                Resource.Status.ERROR -> Unit
                Resource.Status.LOADING -> Unit
            }
        })
    }
}