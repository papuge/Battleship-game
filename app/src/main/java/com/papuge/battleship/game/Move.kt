package com.papuge.battleship.game

data class Move (
    var p1move: Pair<Int, Int> = Pair(-1, -1),   // last move for player 1
    var p2move: Pair<Int, Int> = Pair(-1, -1)
)