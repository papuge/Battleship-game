package com.papuge.battleship.viewModels

import androidx.lifecycle.ViewModel
import com.papuge.battleship.game.Cell
import com.papuge.battleship.game.CellState
import com.papuge.battleship.game.Ship

class GameViewModel: ViewModel() {

    var playerNum: Int = 0
    var userId: String = ""

    var cells: Array<Array<Cell>> = Array(10) { Array(10) {Cell(0, 0)} }
    var ships = mutableListOf<Ship>()

    init {
        for (i in 0..9) {
            for (j in 0..9) {
                cells[i][j] = Cell(i, j)
            }
        }
    }
}