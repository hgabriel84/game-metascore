package com.hgabriel.gamemetascore.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hgabriel.gamemetascore.data.Game
import com.hgabriel.gamemetascore.data.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GameDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    gameRepository: GameRepository
) : ViewModel() {

    private val gameId: Int = savedStateHandle.get<Int>(GAME_ID_SAVED_STATE_KEY)!!

    val uiState: StateFlow<GameDetailUiState> = flow {
        emit(GameDetailUiState.Success(gameRepository.getGame(gameId)))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = GameDetailUiState.Loading
    )

    sealed class GameDetailUiState {
        data class Success(val game: Game) : GameDetailUiState()
        data class Error(val message: String) : GameDetailUiState()
        object Loading : GameDetailUiState()
    }

    companion object {
        private const val GAME_ID_SAVED_STATE_KEY = "gameId"
    }
}
