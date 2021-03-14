package com.wbertan.demo.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.core.content.ContextCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                App()
            }
        }
        window.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background_gradient))
    }
}

@Composable
fun App() {
    SimpleGame(
        Modifier.padding(8.dp)
    )
}

enum class Player {
    X,
    O,
    ;
}

sealed class GameState {
    data class Playing(val player: Player) : GameState()
    data class Win(val player: Player) : GameState()
    object Draw : GameState()
}

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

    fun updateGameState() {
        listOf(Player.X, Player.O).forEach { player ->
            // Rows
            listOf(
                listOf(game[0][0], game[0][1], game[0][2]),
                listOf(game[1][0], game[1][1], game[1][2]),
                listOf(game[2][0], game[2][1], game[2][2]),
            ).forEach { row ->
                if (row.all { it.value == player }) {
                    gameState.value = GameState.Win(player)
                }
            }
            // Columns
            listOf(
                listOf(game[0][0], game[1][0], game[2][0]),
                listOf(game[0][1], game[1][1], game[2][1]),
                listOf(game[0][2], game[1][2], game[2][2]),
            ).forEach { column ->
                if (column.all { it.value == player }) {
                    gameState.value = GameState.Win(player)
                }
            }
            // Diagonal
            listOf(
                listOf(game[0][0], game[1][1], game[2][2]),
                listOf(game[0][2], game[1][1], game[2][0]),
            ).forEach { diagonal ->
                if (diagonal.all { it.value == player }) {
                    gameState.value = GameState.Win(player)
                }
            }
        }
        // Draw
        if (gameState.value is GameState.Playing && game.flatten().none { it.value == null }) {
            gameState.value = GameState.Draw
        }
    }

    fun playTurn(x: Int, y: Int) {
        val player = (gameState.value as? GameState.Playing)?.player ?: return
        val nextPlayer = when (player) {
            Player.X -> Player.O
            Player.O -> Player.X
        }

        game[x][y].value = player
        gameState.value = GameState.Playing(nextPlayer)
        updateGameState()
    }

    fun resetGame() {
        game.flatten().forEach { it.value = null }
        gameState.value = GameState.Playing(Player.X)
    }

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
                onResetGame = ::resetGame
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
                        onClick = { playTurn(x, y) }.takeIf { item.value == null }
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
        style = typography.h6,
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
        style = typography.h6,
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
        style = typography.h6,
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

@Preview(showBackground = true, backgroundColor = 0xFF03A9F4)
@Composable
fun PreviewApp() {
    MaterialTheme {
        App()
    }
}