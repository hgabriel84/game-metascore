package com.hgabriel.gamemetascore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hgabriel.gamemetascore.data.GameAddRepository
import com.hgabriel.gamemetascore.data.MetaGameRepository
import com.hgabriel.gamemetascore.data.MetaGames
import com.hgabriel.gamemetascore.data.Resource
import com.hgabriel.gamemetascore.utilities.toGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GameImportViewModel @Inject constructor(private val repository: MetaGameRepository) :
    ViewModel() {

    private val metaGames: MutableStateFlow<MetaGames> = MutableStateFlow(MetaGames(emptyList()))
    val uiState: StateFlow<GameImportUiState> = metaGames.flatMapLatest { metaGames ->
        flow {
            metaGames.takeIf { it.games.isNotEmpty() }?.let {
                emit(GameImportUiState.Loading)
                var hasError = false
                it.games.forEach { metaGame ->
                    val resource = repository.getGameDetail(metaGame.id)
                    if (resource.status == Resource.Status.SUCCESS) {
                        val game = resource.data!!.toGame()
                        game.apply {
                            liked = metaGame.liked
                            played = metaGame.played
                        }
                        repository.addGame(game)
                    } else {
                        hasError = true
                    }
                }
                if (!hasError) {
                    emit(GameImportUiState.Success)
                } else {
                    emit(GameImportUiState.Error)
                }
            } ?: emit(GameImportUiState.Initial)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GameImportUiState.Initial
    )

    fun importGames(games: MetaGames) {
        metaGames.value = games
    }

    sealed class GameImportUiState {
        object Success : GameImportUiState()
        object Error : GameImportUiState()
        object Initial : GameImportUiState()
        object Loading : GameImportUiState()
    }
}
