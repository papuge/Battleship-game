package com.papuge.battleship.game

data class Game(
    var player1: String = "",
    var player2: String = "",
    var move: Int = 1,
    var p1move: Pair<Int, Int> = Pair(-1, -1),   // last move for player 1
    var p2move: Pair<Int, Int> = Pair(-1, -1),
    var p2answer: String = "",         // hit status: miss, hit, wrecked
    var p1answer: String = "",
    var p1Ready: Boolean = false,
    var p2Ready: Boolean = false,
    var isFinished: Boolean = false
)