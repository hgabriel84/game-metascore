package com.hgabriel.gamemetascore.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hgabriel.gamemetascore.R
import androidx.databinding.DataBindingUtil.setContentView
import com.hgabriel.gamemetascore.databinding.ActivityMetascoreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MetascoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<ActivityMetascoreBinding>(this, R.layout.activity_metascore)
    }
/*
    private val editGameBottomSheetTag = "EditGameBottomSheet"

    private val viewModel by viewModels<GameViewModel>()

    private lateinit var binding: ActivityGamelistBinding
    private lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
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

    private fun initGameList() {
        viewModel.games.observe(this, { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    if (::snackbar.isInitialized) snackbar.dismiss()
                    resource.data?.result?.let { gameAdapter.addAll(it) }
                }
                Resource.Status.ERROR -> showErrorSnackbar()
                Resource.Status.LOADING -> Unit
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

 */
}
