package com.hgabriel.gamemetascore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hgabriel.gamemetascore.data.Game
import com.hgabriel.gamemetascore.data.GameRepository
import com.hgabriel.gamemetascore.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GameDetailViewModel @Inject constructor(gameRepository: GameRepository) : ViewModel() {

    val uiState: StateFlow<GameDetailUiState> = flow {
        // TODO get the game passed by argument to fragment
        emit(GameDetailUiState.Success(gameRepository.getGame(19560)))
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

}
