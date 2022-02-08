package com.hgabriel.gamemetascore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hgabriel.gamemetascore.R
import com.hgabriel.gamemetascore.data.Game
import com.hgabriel.gamemetascore.data.GameOrder
import com.hgabriel.gamemetascore.data.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(private val gameRepository: GameRepository) : ViewModel() {

    private var order = GameOrder.RATING
    private val _uiState: MutableStateFlow<GamesUiState> = MutableStateFlow(GamesUiState.Loading)
    val uiState: StateFlow<GamesUiState> = _uiState

    init {

        viewModelScope.launch {
            /* TODO for testing purposes
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

            gameRepository.addGame(
                Game(
                    id = 136560,
                    name = "EA Sports UFC 4",
                    cover = "https://images.igdb.com/igdb/image/upload/t_cover_big/co2es1.png",
                    summary = "In EA SPORTS UFC 4 the fighter you become is shaped by your fight style, your achievements, and your personality. Develop and customize your character through a unified progression system across all modes. Go from unknown amateur to UFC superstar in the new Career Mode, experience the origins of combat sports in two all-new environments; The Kumite and The Backyard, or challenge the world in new Blitz Battles or Online World Championships to become the undisputed champ. In gameplay, fluid clinch-to-strike combinations offer more responsive and authentic stand-up gameplay, while overhauled takedown and ground mechanics deliver more control in those key phases of the fight. No matter how, or where, you play EA SPORTS UFC 4 puts ‘you’ at the center of every fight.",
                    criticsRating = 79.14f,
                    usersRating = 70.12f,
                    totalRating = 74.63f,
                    played = true,
                    liked = true
                )
            )
            */

            gameRepository.games(order)
                .collect { games -> _uiState.value = GamesUiState.Success(games) }
        }
    }

    fun getOrderIcon(): Int = when (order) {
        GameOrder.RATING -> R.drawable.ic_score_24
        GameOrder.NAME -> R.drawable.ic_alpha_24
    }

    fun toggleOrder() {
        order = when (order) {
            GameOrder.RATING -> GameOrder.NAME
            GameOrder.NAME -> GameOrder.RATING
        }

        viewModelScope.launch {
            gameRepository.games(order)
                .collect { games -> _uiState.value = GamesUiState.Success(games) }
        }
    }

    fun restoreGame(game: Game) = viewModelScope.launch { gameRepository.addGame(game) }

    sealed class GamesUiState {
        data class Success(val games: List<Game>) : GamesUiState()
        object Loading : GamesUiState()
    }
}
