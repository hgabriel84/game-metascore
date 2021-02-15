package com.hgabriel.videogames.scores.ui

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hgabriel.videogames.scores.R
import com.hgabriel.videogames.scores.data.vo.GameOrder
import com.hgabriel.videogames.scores.data.vo.Resource
import com.hgabriel.videogames.scores.databinding.ActivityGamelistBinding
import com.hgabriel.videogames.scores.viewmodel.GamesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GameListActivity : AppCompatActivity() {

    private val addGameBottomSheetTag = "AddGameBottomSheet"

    private val viewModel by viewModels<GamesViewModel>()

    private lateinit var binding: ActivityGamelistBinding
    private lateinit var gameAdapter: GameAdapter
    private lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamelistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLayout()
        initGameList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.game_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_order)?.let { item ->
            viewModel.order.value?.getMenuOrderIcon()?.let { item.setIcon(it) }
        }
        return true
    }

    private fun setupLayout() {
        // recycler view
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

        // listeners
        binding.fab.setOnClickListener { showAddGame() }
    }

    private fun initGameList() {
        viewModel.games.observe(this, { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    if (::snackbar.isInitialized) snackbar.dismiss()
                    resource.data?.result?.let { gameAdapter.addAll(it) }
                }
                Resource.Status.ERROR -> showErrorSnackbar()
                Resource.Status.LOADING -> showLoadingSnackbar()
            }
        })

        viewModel.order.observe(this, { viewModel.fetchGames() })
    }

    private fun showAddGame() {
        AddGameBottomSheet { gamePath ->
            gamePath?.let { viewModel.addGame(it) }
        }.show(supportFragmentManager, addGameBottomSheetTag)
        binding.fab.hide()
    }

    private fun showErrorSnackbar() {
        snackbar = Snackbar.make(binding.coordinatorLayout, R.string.error, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.red_score))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.show()
    }

    private fun showLoadingSnackbar() {
        snackbar = Snackbar
            .make(
                binding.coordinatorLayout,
                R.string.updating_data_label,
                Snackbar.LENGTH_INDEFINITE
            )
            .setBackgroundTint(ContextCompat.getColor(this, R.color.blue))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.show()
    }

    private fun GameOrder.getMenuOrderIcon(): Int =
        when (this) {
            GameOrder.AVERAGE_SCORE -> R.drawable.ic_score_24
            GameOrder.NAME -> R.drawable.ic_alpha_24
        }
}