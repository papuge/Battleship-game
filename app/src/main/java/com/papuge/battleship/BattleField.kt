package com.papuge.battleship

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat


class BattleField: View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val gSize = 10   // grid dimension

    private var cellWidth: Float = 0.0f
    private var cellHeight: Float = 0.0f

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
        drawOne(canvas)
        drawCross(canvas, 2, 0)
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

    fun drawOne(canvas: Canvas) {
        cellWidth = width.toFloat() / gSize
        cellHeight = height.toFloat() / gSize
        canvas.drawCircle(1.5f * cellWidth, 0.5f * cellHeight, 0.3f * cellWidth, hitPaint)
        canvas.drawRect(0.25f * cellWidth, 0.25f * cellHeight, 0.75f * cellWidth, 0.75f * cellHeight, shipPaint)
    }

    fun drawShip(canvas: Canvas, xTouch: Float, yTouch: Float, orientation: Int = 1, rank: Int) {
        val i = (xTouch / cellWidth).toInt()
        val j = (yTouch / cellHeight).toInt()
        val left = (i + 1/4) * cellWidth
        val top = (j + 1/4) * cellHeight
        var right: Float
        var bottom: Float

        if (orientation == 1) {  // horizontal
            right = (i + rank + 3/4) * cellWidth
            bottom = (j + 3/4) * cellHeight
        } else {
            right = (i + 3/4) * cellWidth
            bottom = (j + rank + 3/4) * cellHeight
        }

    }

    fun drawCross(canvas: Canvas, i: Int, j: Int) {
        canvas.drawLine((i + 0.2f) * cellWidth, (j + 0.2f) * cellHeight,
            (i + 0.8f) * cellWidth, (j + 0.8f) * cellHeight, missPaint)

        canvas.drawLine((i + 0.8f) * cellWidth, (j + 0.2f) * cellHeight,
            (i + 0.2f) * cellWidth, (j + 0.8f) * cellHeight, missPaint)
    }

}