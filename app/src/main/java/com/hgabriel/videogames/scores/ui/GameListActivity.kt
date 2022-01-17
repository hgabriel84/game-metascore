package com.hgabriel.videogames.scores.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hgabriel.videogames.scores.R
import com.hgabriel.videogames.scores.data.Game
import com.hgabriel.videogames.scores.data.GameOrder
import com.hgabriel.videogames.scores.databinding.ActivityGamelistBinding
import com.hgabriel.videogames.scores.viewmodel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameListActivity : AppCompatActivity() {

    private val editGameBottomSheetTag = "EditGameBottomSheet"

    private val viewModel by viewModels<GameViewModel>()

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
            viewModel.getOrder().getMenuOrderIcon().let { item.setIcon(it) }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_order -> {
                viewModel.toggleOrder()
                invalidateOptionsMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupLayout() {
        // recycler view
        gameAdapter = GameAdapter(arrayListOf()) { showEditGame(it) }
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rvGames.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(this.context, linearLayoutManager.orientation))
            adapter = gameAdapter
        }
    }

    private fun initGameList() {
        /*
        viewModel.games.observe(this, { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    if (::snackbar.isInitialized) snackbar.dismiss()
                    resource.data?.result?.let { gameAdapter.addAll(it) }
                    binding.tvEmptyState.visibility = if (gameAdapter.itemCount == 0) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
                Resource.Status.ERROR -> showErrorSnackbar()
                Resource.Status.LOADING -> Unit
            }
        })
         */
        viewModel.games.observe(this, {
            gameAdapter.addAll(it)
            binding.tvEmptyState.visibility = if (gameAdapter.itemCount == 0) {
                View.VISIBLE
            } else {
                View.GONE
            }
        })
        // viewModel.deletedGame.observe(this, { it?.let { showRestoreGameSnackbar() } })
    }

    private fun showEditGame(game: Game) {
        EditGameBottomSheet(
            game,
            onPlayed = { },
            onLiked = { },
            onDelete = { }
        ).show(supportFragmentManager, editGameBottomSheetTag)
    }

    private fun showErrorSnackbar() {
        snackbar = Snackbar.make(binding.coordinatorLayout, R.string.error, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.snackbar_error_bg))
            .setTextColor(ContextCompat.getColor(this, R.color.snackbar_label))
        snackbar.show()
    }

    private fun showRestoreGameSnackbar() {
        snackbar = Snackbar
            .make(binding.coordinatorLayout, R.string.game_deleted, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.snackbar_restore_bg))
            .setTextColor(ContextCompat.getColor(this, R.color.snackbar_label))
            .setAction(R.string.undo) {}  // { viewModel.restoreGame() }
            .setActionTextColor(ContextCompat.getColor(this, R.color.snackbar_label))
        snackbar.show()
    }

    private fun GameOrder.getMenuOrderIcon(): Int =
        when (this) {
            GameOrder.RATING -> R.drawable.ic_score_24
            GameOrder.NAME -> R.drawable.ic_alpha_24
        }
}
