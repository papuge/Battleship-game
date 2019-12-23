package com.papuge.battleship.game

data class Cell(
    val x: Int,
    val y: Int,
    var state: CellState = CellState.EMPTY,
    var isShip: Boolean = false
)