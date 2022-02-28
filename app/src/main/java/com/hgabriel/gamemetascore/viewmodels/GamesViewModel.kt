package com.hgabriel.gamemetascore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hgabriel.gamemetascore.R
import com.hgabriel.gamemetascore.data.Game
import com.hgabriel.gamemetascore.data.GameOrder
import com.hgabriel.gamemetascore.data.GamesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(private val repository: GamesRepository) : ViewModel() {

    private val order: MutableStateFlow<GameOrder> = MutableStateFlow(GameOrder.RATING)
    val uiState: StateFlow<GamesUiState> = order.flatMapLatest { order ->
        repository.games(order).map { GamesUiState.Success(it, getOrderIcon()) }
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

    private fun getOrderIcon(): Int = when (order.value) {
        GameOrder.RATING -> R.drawable.ic_score_24
        GameOrder.NAME -> R.drawable.ic_alpha_24
    }

    sealed class GamesUiState {
        data class Success(val games: List<Game>, val orderIcon: Int) : GamesUiState()
        object Loading : GamesUiState()
    }

}
