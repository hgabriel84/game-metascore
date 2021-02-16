package com.hgabriel.videogames.scores.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hgabriel.videogames.scores.data.repository.GameRepository
import com.hgabriel.videogames.scores.data.vo.Game
import com.hgabriel.videogames.scores.data.vo.GameOrder
import com.hgabriel.videogames.scores.data.vo.GamesResponse
import com.hgabriel.videogames.scores.data.vo.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(private val repo: GameRepository) : ViewModel() {

    val games: MutableLiveData<Resource<GamesResponse>> by lazy {
        MutableLiveData<Resource<GamesResponse>>()
    }

    val order: MutableLiveData<GameOrder> by lazy {
        MutableLiveData<GameOrder>()
    }

    val deletedGame: MutableLiveData<Game?> by lazy {
        MutableLiveData<Game?>()
    }

    init {
        order.value = GameOrder.AVERAGE_SCORE
        fetchGames()
    }

    fun addGame(gamePath: String) {
        viewModelScope.launch {
            repo.addGame(gamePath, order.value ?: GameOrder.AVERAGE_SCORE)
                .collect { games.value = it }
        }
    }

    fun deleteGame(game: Game) {
        viewModelScope.launch {
            repo.deleteGame(game, order.value ?: GameOrder.AVERAGE_SCORE)
                .collect { games.value = it }
            deletedGame.value = game
        }
    }

    fun restoreGame() {
        viewModelScope.launch {
            deletedGame.value?.let { game ->
                repo.addGame(game, order.value ?: GameOrder.AVERAGE_SCORE)
                    .collect { games.value = it }
            }
            deletedGame.value = null
        }
    }

    fun togglePlayed(game: Game) {
        viewModelScope.launch {
            game.played = !game.played
            repo.addGame(game, order.value ?: GameOrder.AVERAGE_SCORE)
                .collect { games.value = it }
        }
    }

    fun toggleOrder() {
        viewModelScope.launch {
            order.value?.let { gameOrder ->
                when (gameOrder) {
                    GameOrder.AVERAGE_SCORE -> order.value = GameOrder.NAME
                    GameOrder.NAME -> order.value = GameOrder.AVERAGE_SCORE
                }
            }
            repo.loadGamesFromDb(order.value ?: GameOrder.AVERAGE_SCORE)
                .collect { games.value = it }
        }
    }

    private fun fetchGames() {
        viewModelScope.launch {
            repo.fetchGames(order.value ?: GameOrder.AVERAGE_SCORE)
                .collect { games.value = it }
        }
    }
}