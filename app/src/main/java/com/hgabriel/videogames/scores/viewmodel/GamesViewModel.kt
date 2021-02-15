package com.hgabriel.videogames.scores.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hgabriel.videogames.scores.data.repository.GameRepository
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

    fun fetchGames() {
        viewModelScope.launch {
            repo.fetchGames(order.value ?: GameOrder.AVERAGE_SCORE).collect { games.value = it }
        }
    }

}