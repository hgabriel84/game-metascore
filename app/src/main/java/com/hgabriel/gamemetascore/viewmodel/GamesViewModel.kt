package com.hgabriel.gamemetascore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hgabriel.gamemetascore.data.Game
import com.hgabriel.gamemetascore.data.GameOrder
import com.hgabriel.gamemetascore.data.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    gameRepository: GameRepository//,
    // private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val order: MutableStateFlow<GameOrder> = MutableStateFlow(
        GameOrder.RATING //savedStateHandle.get(GAME_ORDER_SAVED_STATE_KEY) ?: GameOrder.RATING
    )
/*
    val games: LiveData<List<Game>> = order.flatMapLatest {
        gameRepository.getGames(it)
    }.asLiveData()

 */

    val uiState: StateFlow<GamesUiState> = flow {
        emit(GamesUiState.Success(gameRepository.getGames(GameOrder.RATING)))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily, // SharingStarted.WhileSubscribed(5000),
        initialValue = GamesUiState.Loading
    )

    sealed class GamesUiState {
        data class Success(val games: List<Game>) : GamesUiState()
        data class Error(val message: String) : GamesUiState()
        object Loading : GamesUiState()
    }

    init {
        /*
        viewModelScope.launch {
            order.collect { newOrder ->
                savedStateHandle.set(GAME_ORDER_SAVED_STATE_KEY, newOrder)
            }
        }
         */
    }
/*
    fun getOrder(): GameOrder = order.value

    fun toggleOrder() {
        when (order.value) {
            GameOrder.RATING -> order.value = GameOrder.NAME
            GameOrder.NAME -> order.value = GameOrder.RATING
        }
    }

    companion object {
        private const val GAME_ORDER_SAVED_STATE_KEY = "GAME_ORDER_SAVED_STATE_KEY"
    }

 */
}
