package com.papuge.battleship

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.papuge.battleship.game.Cell
import com.papuge.battleship.game.CellState
import com.papuge.battleship.game.Orientation


class BattleField: View {

    @SuppressLint("Recycle")
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val a = context!!.obtainStyledAttributes(attrs, R.styleable.BattleField)

        withShips = a.getBoolean(R.styleable.BattleField_withShips, false)
    }

    private val gSize = 10   // grid dimension
    private var withShips: Boolean = false

    var shipRects = mutableListOf<RectF>()

    var cells = Array(10) { Array(10) {Cell(0, 0)} }

    var cellWidth: Float = 0.0f
    var cellHeight: Float = 0.0f

    private val gridPaint = Paint()
    private val shipPaint = Paint()
    private val hitPaint = Paint()
    private val missPaint = Paint()

    init {

        gridPaint.color =
            ContextCompat.getColor(context, R.color.colorPrimary)
        gridPaint.isAntiAlias = true
        gridPaint.style = Paint.Style.STROKE
        gridPaint.strokeWidth = resources.displayMetrics.density * 2

        shipPaint.color =
            ContextCompat.getColor(context, R.color.colorAccent)
        shipPaint.isAntiAlias = true
        shipPaint.style = Paint.Style.FILL
        shipPaint.strokeWidth = resources.displayMetrics.density * 2

        hitPaint.color =
            ContextCompat.getColor(context, R.color.colorHit)
        hitPaint.isAntiAlias = true
        hitPaint.style = Paint.Style.FILL
        hitPaint.strokeWidth = resources.displayMetrics.density * 2

        missPaint.color =
            ContextCompat.getColor(context, R.color.colorMiss)
        missPaint.isAntiAlias = true
        missPaint.style = Paint.Style.FILL
        missPaint.strokeWidth = resources.displayMetrics.density * 4
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGrid(canvas)
        if (withShips) {
            drawShips(canvas)
        }
        drawCells(canvas)
    }


    private fun drawGrid(canvas: Canvas) {
        cellWidth = width.toFloat() / gSize
        cellHeight = height.toFloat() / gSize

        // vertical lines
        for (i in 0..gSize) {
            canvas.drawLine(i * cellWidth, 0f, i * cellWidth, height.toFloat(), gridPaint)
        }

        // horizontal lines
        for (i in 0..gSize) {
            canvas.drawLine(0f, i * cellHeight, width.toFloat(), i * cellHeight, gridPaint)
        }
    }

    fun addShip(i: Int, j: Int, orientation: Orientation, rank: Int) {
        val left = (i + 0.25f) * cellWidth
        val top = (j + 0.25f) * cellHeight
        val right: Float
        val bottom: Float

        if (orientation == Orientation.HORIZONTAL) {  // horizontal
            right = (i + rank - 1 + 0.75f) * cellWidth
            bottom = (j + 0.75f) * cellHeight
        } else {
            right = (i + 0.75f) * cellWidth
            bottom = (j + rank - 1 + 0.75f) * cellHeight
        }
        shipRects.add(RectF(left, top, right, bottom))
        refreshCanvas()
    }

    private fun drawShips(canvas: Canvas) {
        for (rect in shipRects) {
            canvas.drawRect(rect, shipPaint)
        }
    }

    private fun drawCells(canvas: Canvas) {
        cellWidth = width.toFloat() / gSize
        cellHeight = height.toFloat() / gSize
        for(i in 1 until 10) {
            for (j in 1 until 10) {
                if(cells[i][j].state == CellState.MISS) {
                    drawMiss(canvas, i, j)
                }
                else if(cells[i][j].state == CellState.HIT) {
                    drawHit(canvas, i, j)
                }
            }
        }
    }

    private fun drawMiss(canvas: Canvas, i: Int, j: Int) {
        canvas.drawLine((i + 0.2f) * cellWidth, (j + 0.2f) * cellHeight,
            (i + 0.8f) * cellWidth, (j + 0.8f) * cellHeight, missPaint)

        canvas.drawLine((i + 0.8f) * cellWidth, (j + 0.2f) * cellHeight,
            (i + 0.2f) * cellWidth, (j + 0.8f) * cellHeight, missPaint)
    }

    private fun drawHit(canvas: Canvas, i: Int, j: Int) {
        canvas.drawCircle((i + 0.5f) * cellWidth, (j + 0.5f) * cellHeight, 0.3f * cellWidth, hitPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }

    fun refreshCanvas() = invalidate()
}