package com.hgabriel.gamemetascore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hgabriel.gamemetascore.data.GameAddRepository
import com.hgabriel.gamemetascore.data.IgdbGame
import com.hgabriel.gamemetascore.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameAddViewModel @Inject constructor(private val repository: GameAddRepository) :
    ViewModel() {

    private val keyword: MutableStateFlow<String> = MutableStateFlow("")
    val uiState: Flow<GameAddUiState> = keyword.mapLatest { keyword ->
        val resource = repository.searchGame(keyword)
        if (resource.status == Resource.Status.SUCCESS) {
            GameAddUiState.Success(resource.data!!)
        } else {
            GameAddUiState.Error(resource.message!!)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GameAddUiState.Loading
    )

    fun search(s: String) {
        keyword.value = s
    }

    fun addGame(igdbGame: IgdbGame) {
        viewModelScope.launch {
            repository.addGame(igdbGame)
        }
    }

    sealed class GameAddUiState {
        data class Success(val games: List<IgdbGame>) : GameAddUiState()
        data class Error(val message: String) : GameAddUiState()
        object Loading : GameAddUiState()
    }
}