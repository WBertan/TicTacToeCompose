package com.wbertan.demo.tictactoe.game

import androidx.compose.runtime.MutableState
import com.wbertan.demo.tictactoe.game.data.GameState
import com.wbertan.demo.tictactoe.game.data.Player

class SimpleGameManager(
    private val game: List<List<MutableState<Player?>>>,
    private val gameState: MutableState<GameState>,
) {
    private fun updateGameState() {
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

    private fun autoPlay() {
        // Rows
        val rowsMovements = game.mapIndexed { x, row ->
            val xCount = row.count { it.value == Player.X }
            val oCount = row.count { it.value == Player.O }
            val emptyCount = row.count { it.value == null }

            val yList = mutableListOf<Pair<Int, Int>>()

            // Can I win?
            if (oCount == 2 && emptyCount == 1) {
                yList.add(3 to row.indexOfFirst { it.value == null })
            }
            // Can I block?
            if (xCount == 2 && emptyCount == 1) {
                yList.add(2 to row.indexOfFirst { it.value == null })
            }
            // Can I improve?
            if (oCount == 1 && emptyCount >= 1) {
                yList.addAll(
                    row.mapIndexedNotNull { index, it -> if (it.value == null) 1 to index else null }
                )
            }
            // Empty
            if (emptyCount > 0) {
                yList.addAll(row.mapIndexedNotNull { index, it -> if (it.value == null) 0 to index else null })
            }
            yList.map { (weight, y) -> weight to (x to y) }
        }.flatten()

        //Columns
        val columnsMovements = game.transpose().mapIndexed { y, column ->
            val xCount = column.count { it.value == Player.X }
            val oCount = column.count { it.value == Player.O }
            val emptyCount = column.count { it.value == null }

            val xList = mutableListOf<Pair<Int, Int>>()

            // Can I win?
            if (oCount == 2 && emptyCount == 1) {
                xList.add(3 to column.indexOfFirst { it.value == null })
            }
            // Can I block?
            if (xCount == 2 && emptyCount == 1) {
                xList.add(2 to column.indexOfFirst { it.value == null })
            }
            // Can I improve?
            if (oCount == 1 && emptyCount >= 1) {
                xList.addAll(
                    column.mapIndexedNotNull { index, it -> if (it.value == null) 1 to index else null }
                )
            }
            // Empty
            if (emptyCount > 0) {
                xList.addAll(column.mapIndexedNotNull { index, it -> if (it.value == null) 0 to index else null })
            }
            xList.map { (weight, x) -> weight to (x to y) }
        }.flatten()

        // Diagonal
        val diagonalMovements = listOf(
            listOf(Pair(0, 0) to game[0][0], Pair(1, 1) to game[1][1], Pair(2, 2) to game[2][2]),
            listOf(Pair(0, 2) to game[0][2], Pair(1, 1) to game[1][1], Pair(2, 0) to game[2][0]),
        ).map { diagonal ->
            val xCount = diagonal.count { it.second.value == Player.X }
            val oCount = diagonal.count { it.second.value == Player.O }
            val emptyCount = diagonal.count { it.second.value == null }

            val xyList = mutableListOf<Pair<Int, Pair<Int, Int>>>()

            // Can I win?
            if (oCount == 2 && emptyCount == 1) {
                xyList.add(3 to diagonal.first { it.second.value == null }.first)
            }
            // Can I block?
            if (xCount == 2 && emptyCount == 1) {
                xyList.add(2 to diagonal.first { it.second.value == null }.first)
            }
            // Can I improve?
            if (oCount == 1 && emptyCount >= 1) {
                xyList.addAll(
                    diagonal.mapNotNull { (index, it) -> if (it.value == null) 1 to index else null }
                )
            }
            // Empty
            if (emptyCount > 0) {
                xyList.addAll(diagonal.mapNotNull { (index, it) -> if (it.value == null) 0 to index else null })
            }
            xyList
        }.flatten()

        val movements = (rowsMovements + columnsMovements + diagonalMovements)
            .groupBy(keySelector = { it.first }) { it.second }

        val winMovements = movements[3]
        val blockMovements = movements[2]
        val improveMovements = movements[1]
        val freeMovements = movements[0] ?: error("Should always have at least 1 free movement!")

        val (nextMovementX, nextMovementY) =
            winMovements?.random()
                ?: blockMovements?.random()
                ?: improveMovements?.random()
                ?: freeMovements.random()
        playTurn(nextMovementX, nextMovementY)
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
        if (gameState.value is GameState.Playing && nextPlayer == Player.O) {
            autoPlay()
        }
    }

    fun resetGame() {
        game.flatten().forEach { it.value = null }
        gameState.value = GameState.Playing(Player.X)
    }

    private fun <T> List<List<T>>.transpose(): List<List<T>> {
        val result = (first().indices).map { mutableListOf<T>() }.toMutableList()
        forEach { list -> result.zip(list).forEach { it.first.add(it.second) } }
        return result
    }
}