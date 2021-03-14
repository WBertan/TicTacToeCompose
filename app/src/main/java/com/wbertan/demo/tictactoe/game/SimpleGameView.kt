package com.wbertan.demo.tictactoe.game

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState
import com.wbertan.demo.tictactoe.R
import com.wbertan.demo.tictactoe.game.data.GameState
import com.wbertan.demo.tictactoe.game.data.Player

object SimpleGameView {

    @Composable
    fun SimpleGame(modifier: Modifier = Modifier) {
        val game: List<List<MutableState<Player?>>> = listOf(
            listOf(
                remember { mutableStateOf(null) },
                remember { mutableStateOf(null) },
                remember { mutableStateOf(null) }
            ),
            listOf(
                remember { mutableStateOf(null) },
                remember { mutableStateOf(null) },
                remember { mutableStateOf(null) }
            ),
            listOf(
                remember { mutableStateOf(null) },
                remember { mutableStateOf(null) },
                remember { mutableStateOf(null) }
            ),
        )

        val gameState: MutableState<GameState> = remember { mutableStateOf(GameState.Playing(Player.X)) }

        val simpleGameManager = SimpleGameManager(game, gameState)

        Column(modifier.fillMaxHeight()) {
            Row(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                State(
                    Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .align(Alignment.CenterVertically),
                    value = gameState.value,
                    onResetGame = simpleGameManager::resetGame
                )
            }
            game.fastForEachIndexed { x, row ->
                Row(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    row.fastForEachIndexed { y, item ->
                        Item(
                            Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .align(Alignment.CenterVertically),
                            value = item.value,
                            onClick = { simpleGameManager.playTurn(x, y) }.takeIf { item.value == null }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun State(
        modifier: Modifier,
        value: GameState,
        onResetGame: () -> Unit,
    ) {
        val onClick = onResetGame.takeIf { value !is GameState.Playing }
        Row(
            modifier
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .border(width = 4.dp, color = Color.White, shape = RoundedCornerShape(16.dp))
                .run {
                    if (onClick != null) {
                        this.clickable(onClick = { onClick.invoke() })
                    } else {
                        this
                    }
                }
        ) {
            when (value) {
                GameState.Draw -> StateDraw(modifier)
                is GameState.Playing -> StatePlaying(modifier, value)
                is GameState.Win -> StateWin(modifier, value)
            }
        }
    }

    @Composable
    fun RowScope.StatePlaying(modifier: Modifier, value: GameState) {
        Text(
            "Next move:",
            modifier
                .wrapContentSize(Alignment.Center)
                .weight(2f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
            color = Color.White,
        )
        Box(
            modifier
                .padding(8.dp)
                .weight(1f)
        ) {
            when ((value as? GameState.Playing)?.player) {
                Player.O -> ItemO(Modifier)
                Player.X -> ItemX(Modifier)
            }
        }
    }

    @Composable
    fun RowScope.StateWin(modifier: Modifier, value: GameState) {
        Box(
            modifier
                .padding(8.dp)
                .weight(1f)
        ) {
            when ((value as? GameState.Win)?.player) {
                Player.O -> ItemO(Modifier)
                Player.X -> ItemX(Modifier)
            }
        }
        Text(
            "Wins!\nClick here to reset",
            modifier
                .wrapContentSize(Alignment.Center)
                .weight(2f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
            color = Color.White,
        )
    }

    @Composable
    fun RowScope.StateDraw(modifier: Modifier) {
        Text(
            "It is a Draw!\nClick here to reset",
            modifier
                .wrapContentSize(Alignment.Center)
                .weight(2f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
            color = Color.White,
        )
    }

    @Composable
    fun Item(
        modifier: Modifier = Modifier,
        value: Player?,
        onClick: (() -> Unit)?
    ) {
        Box(
            modifier
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .border(width = 4.dp, color = Color.White, shape = RoundedCornerShape(16.dp))
                .run {
                    if (onClick != null) {
                        this.clickable(onClick = { onClick.invoke() })
                    } else {
                        this
                    }
                }
                .wrapContentSize(Alignment.Center)
        ) {
            when (value) {
                Player.O -> ItemO(Modifier)
                Player.X -> ItemX(Modifier)
            }
        }
    }

    @Composable
    fun ItemO(modifier: Modifier) {
        val animationSpec = remember { LottieAnimationSpec.RawRes(R.raw.o) }
        val animationState = rememberLottieAnimationState(autoPlay = true)
        LottieAnimation(
            animationSpec,
            modifier.fillMaxSize(),
            animationState,
        )
    }

    @Composable
    fun ItemX(modifier: Modifier) {
        val animationSpec = remember { LottieAnimationSpec.RawRes(R.raw.x) }
        val animationState = rememberLottieAnimationState(autoPlay = true)
        LottieAnimation(
            animationSpec,
            modifier.fillMaxSize(),
            animationState,
        )
    }
}