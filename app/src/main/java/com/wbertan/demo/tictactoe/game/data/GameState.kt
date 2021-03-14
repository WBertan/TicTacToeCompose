package com.wbertan.demo.tictactoe.game.data

sealed class GameState {
    data class Playing(val player: Player) : GameState()
    data class Win(val player: Player) : GameState()
    object Draw : GameState()
}