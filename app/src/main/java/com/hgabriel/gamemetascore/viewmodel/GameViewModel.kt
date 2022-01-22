package com.hgabriel.gamemetascore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hgabriel.gamemetascore.data.Game
import com.hgabriel.gamemetascore.data.GameOrder
import com.hgabriel.gamemetascore.data.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    gameRepository: GameRepository//,
    // private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val order: MutableStateFlow<GameOrder> = MutableStateFlow(
        GameOrder.RATING //savedStateHandle.get(GAME_ORDER_SAVED_STATE_KEY) ?: GameOrder.RATING
    )

    val games: LiveData<List<Game>> = order.flatMapLatest {
        gameRepository.getGames(it)
    }.asLiveData()

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
