package com.papuge.battleship.viewModels

import android.graphics.RectF
import androidx.lifecycle.ViewModel
import com.papuge.battleship.game.Cell
import com.papuge.battleship.game.Game
import com.papuge.battleship.game.Ship

class GameViewModel: ViewModel() {

    var playerNum: Int = 0
    var userId: String = ""
    var gameId: Int = -1
    var game: Game? = null

    var cells: Array<Array<Cell>> = Array(10) { Array(10) {Cell(0, 0)} }
    var ships = mutableListOf<Ship>()
    var shipRects = mutableListOf<RectF>()

    init {
        for (i in 0..9) {
            for (j in 0..9) {
                cells[i][j] = Cell(i, j)
            }
        }
    }

    fun clear() {
        playerNum = 0
        gameId = 0
        game = null
        cells = Array(10) { Array(10) {Cell(0, 0)} }
        ships = mutableListOf()
        shipRects = mutableListOf()
    }
}