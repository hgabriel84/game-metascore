package com.hgabriel.gamemetascore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hgabriel.gamemetascore.R
import com.hgabriel.gamemetascore.data.Game
import com.hgabriel.gamemetascore.data.GameOrder
import com.hgabriel.gamemetascore.data.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    gameRepository: GameRepository
) : ViewModel() {

    private val order: MutableStateFlow<GameOrder> = MutableStateFlow(GameOrder.RATING)

    val uiState: StateFlow<GamesUiState> = order.transformLatest {
        emit(GamesUiState.Success(gameRepository.getGames(order.value)))
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = GamesUiState.Loading
    )

    sealed class GamesUiState {
        data class Success(val games: List<Game>) : GamesUiState()
        data class Error(val message: String) : GamesUiState()
        object Loading : GamesUiState()
    }

    fun getOrderIcon(): Int = when (order.value) {
        GameOrder.RATING -> R.drawable.ic_score_24
        GameOrder.NAME -> R.drawable.ic_alpha_24
    }

    fun toggleOrder() {
        when (order.value) {
            GameOrder.RATING -> order.value = GameOrder.NAME
            GameOrder.NAME -> order.value = GameOrder.RATING
        }
    }
}
