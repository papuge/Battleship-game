package com.papuge.battleship.game

data class Ship(
    val rank: Int,
    val orientation: Orientation,
    var cells: MutableList<Cell> = mutableListOf()
) {
    fun isKilled(): Boolean {
        return cells.all { cell -> cell.state == CellState.HIT }
    }
}