package com.hgabriel.gamemetascore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hgabriel.gamemetascore.R
import com.hgabriel.gamemetascore.data.Game
import com.hgabriel.gamemetascore.data.GameOrder
import com.hgabriel.gamemetascore.data.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(private val repository: GameRepository) : ViewModel() {

    private val order = MutableStateFlow(GameOrder.RATING)
    private val keyword = MutableStateFlow("")
    val uiState = order.flatMapLatest { order ->
        keyword.flatMapLatest { keyword ->
            if (keyword.isEmpty()) {
                repository.games(order).map { getGames(it) }
            } else {
                repository.games(order, keyword).map { getGamesByKeyword(it) }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = GamesUiState.Loading
    )

    fun toggleOrder() {
        when (order.value) {
            GameOrder.RATING -> order.value = GameOrder.NAME
            GameOrder.NAME -> order.value = GameOrder.RATING
        }
    }

    fun restoreGame(game: Game) = viewModelScope.launch { repository.addGame(game) }
    fun search(s: String) {
        keyword.value = s
    }

    private fun getOrderIcon() =
        when (order.value) {
            GameOrder.RATING -> R.drawable.ic_score_24
            GameOrder.NAME -> R.drawable.ic_alpha_24
        }

    private fun getGames(games: List<Game>) =
        if (games.isEmpty()) GamesUiState.Empty else GamesUiState.Success(games, getOrderIcon())

    private fun getGamesByKeyword(games: List<Game>) =
        if (games.isEmpty()) GamesUiState.NoResults else GamesUiState.Success(games, getOrderIcon())

    sealed class GamesUiState {
        data class Success(val games: List<Game>, val orderIcon: Int) : GamesUiState()
        object Loading : GamesUiState()
        object Empty : GamesUiState()
        object NoResults : GamesUiState()
    }
}
