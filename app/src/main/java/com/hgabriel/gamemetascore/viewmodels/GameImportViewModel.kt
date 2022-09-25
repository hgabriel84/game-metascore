package com.hgabriel.gamemetascore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hgabriel.gamemetascore.data.GameRepository
import com.hgabriel.gamemetascore.data.MetaGames
import com.hgabriel.gamemetascore.data.Resource
import com.hgabriel.gamemetascore.utilities.toGame
import com.hgabriel.gamemetascore.utilities.toMetaGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameImportViewModel @Inject constructor(private val repository: GameRepository) :
    ViewModel() {

    private val importGames = MutableStateFlow(MetaGames(emptyList()))
    val importUiState = importGames.flatMapLatest { metaGames ->
        flow {
            metaGames.takeIf { it.games.isNotEmpty() }?.let {
                emit(GameImportUiState.Loading)
                it.games.forEach { metaGame ->
                    val resource = repository.gameDetail(metaGame.id)
                    if (resource.status == Resource.Status.SUCCESS) {
                        val game = resource.data!!.toGame()
                        game.apply {
                            liked = metaGame.liked
                            played = metaGame.played
                        }
                        repository.addGame(game)
                    }
                }
                emit(GameImportUiState.Initial)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = GameImportUiState.Initial
    )
    lateinit var exportGames: MetaGames

    init {
        viewModelScope.launch {
            exportGames = MetaGames(games = repository.games().map { it.toMetaGame() })
        }
    }

    fun importGames(games: MetaGames) {
        importGames.value = games
    }

    sealed class GameImportUiState {
        object Initial : GameImportUiState()
        object Loading : GameImportUiState()
    }
}
