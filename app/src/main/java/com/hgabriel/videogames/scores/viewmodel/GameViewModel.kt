package com.hgabriel.videogames.scores.viewmodel

import androidx.lifecycle.*
import com.hgabriel.videogames.scores.data.Game
import com.hgabriel.videogames.scores.data.GameOrder
import com.hgabriel.videogames.scores.data.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var order: MutableStateFlow<GameOrder> = MutableStateFlow(
        savedStateHandle.get(GAME_ORDER_SAVED_STATE_KEY) ?: GameOrder.RATING
    )

    val games: LiveData<List<Game>> = gameRepository.getGames(order.value).asLiveData()


    init {
        viewModelScope.launch {
            order.collect { newOrder ->
                savedStateHandle.set(GAME_ORDER_SAVED_STATE_KEY, newOrder)
            }
        }
    }

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
}
