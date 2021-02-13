package com.hgabriel.videogames.scores.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hgabriel.videogames.scores.data.repository.GameRepository
import com.hgabriel.videogames.scores.data.vo.GamesResponse
import com.hgabriel.videogames.scores.data.vo.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(private val repo: GameRepository) : ViewModel() {

    var games = MutableLiveData<Resource<GamesResponse>>()
        private set

    init {
        fetchGames()
    }

    private fun fetchGames() {
        viewModelScope.launch {
            repo.fetchGames().collect { games.value = it }
        }
    }

}