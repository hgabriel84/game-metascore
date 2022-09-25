package com.hgabriel.gamemetascore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hgabriel.gamemetascore.data.GameRepository
import com.hgabriel.gamemetascore.data.IgdbGame
import com.hgabriel.gamemetascore.data.Resource
import com.hgabriel.gamemetascore.utilities.toGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameAddViewModel @Inject constructor(private val repository: GameRepository) :
    ViewModel() {

    private val keyword = MutableStateFlow("")
    val uiState = keyword.flatMapLatest { keyword ->
        flow {
            keyword.takeIf { it.isNotEmpty() }?.let {
                emit(GameAddUiState.Loading)
                val resource = repository.searchGame(it)
                if (resource.status == Resource.Status.SUCCESS) {
                    emit(getGames(resource.data!!))
                } else {
                    emit(GameAddUiState.Error(resource.message!!))
                }
            } ?: emit(GameAddUiState.Initial)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GameAddUiState.Initial
    )

    private fun getGames(games: List<IgdbGame>) =
        if (games.isEmpty()) GameAddUiState.NoResults else GameAddUiState.Success(games)

    fun search(s: String) {
        keyword.value = s
    }

    fun addGame(igdbGame: IgdbGame) =
        viewModelScope.launch { repository.addGame(igdbGame.toGame()) }

    sealed class GameAddUiState {
        data class Success(val games: List<IgdbGame>) : GameAddUiState()
        data class Error(val message: String) : GameAddUiState()
        object Loading : GameAddUiState()
        object Initial : GameAddUiState()
        object NoResults : GameAddUiState()
    }
}
