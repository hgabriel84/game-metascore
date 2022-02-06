package com.hgabriel.gamemetascore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hgabriel.gamemetascore.R
import com.hgabriel.gamemetascore.data.Game
import com.hgabriel.gamemetascore.data.GameOrder
import com.hgabriel.gamemetascore.data.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(gameRepository: GameRepository) : ViewModel() {

    // TODO for testing purposes
    init {
        viewModelScope.launch {
            gameRepository.addGame(
                Game(
                    id = 19560,
                    name = "God of War",
                    cover = "https://images.igdb.com/igdb/image/upload/t_cover_big/co1tmu.png",
                    summary = "God of War is the sequel to God of War III as well as a continuation of the canon God of War chronology. Unlike previous installments, this game focuses on Norse mythology and follows an older and more seasoned Kratos and his son Atreus in the years since the third game. It is in this harsh, unforgiving world that he must fight to survive… and teach his son to do the same.",
                    storyline = "Many years have passed since Kratos, Spartan warrior and former Greek God of War, took his vengeance against the Greek Gods, and he now lives with his young son Atreus in ancient Norway in the realm of Midgard. The game begins after the death of the Jötunn warrior Faye, Kratos' second wife and Atreus' mother, whose last request was for her ashes to be spread at the highest speak of the nine realms. Kratos and Atreus prepare a funeral pyre for her, mourn her death and soon go on a hunt as per Kratos' desire. However much to Kratos' disappointment, Atreus proves his incompetence and lack of focus, making Kratos reconsider taking Atreus in his journey. Kratos is soon attacked by a mysterious stranger with godlike powers, and who cannot feel anything physically. After seemingly killing him, Kratos reluctantly takes Atreus with him and begins their journey.",
                    criticsRating = 96.23f,
                    usersRating = 94.87f,
                    totalRating = 95.55f,
                    played = false,
                    liked = true
                )
            )
        }
    }

    private val order: MutableStateFlow<GameOrder> = MutableStateFlow(GameOrder.RATING)

    val uiState: StateFlow<GamesUiState> = order.transformLatest {
        emit(GamesUiState.Success(gameRepository.getGames(order.value)))
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = GamesUiState.Loading
    )

    sealed class GamesUiState {
        data class Success(val games: List<Game>) : GamesUiState()
        data class Error(val message: String) : GamesUiState()
        object Loading : GamesUiState()
    }

    fun getOrderIcon(): Int = when (order.value) {
        GameOrder.RATING -> R.drawable.ic_score_24
        GameOrder.NAME -> R.drawable.ic_alpha_24
    }

    fun toggleOrder() {
        when (order.value) {
            GameOrder.RATING -> order.value = GameOrder.NAME
            GameOrder.NAME -> order.value = GameOrder.RATING
        }
    }
}
