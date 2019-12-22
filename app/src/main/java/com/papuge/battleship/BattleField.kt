package com.papuge.battleship

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
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
        shipPaint.style = Paint.Style.STROKE
        shipPaint.strokeWidth = resources.displayMetrics.density * 2

        hitPaint.color =
            ContextCompat.getColor(context, R.color.colorHit)
        hitPaint.isAntiAlias = true
        hitPaint.style = Paint.Style.STROKE
        hitPaint.strokeWidth = resources.displayMetrics.density * 2

        missPaint.color =
            ContextCompat.getColor(context, R.color.colorMiss)
        missPaint.isAntiAlias = true
        missPaint.style = Paint.Style.STROKE
        missPaint.strokeWidth = resources.displayMetrics.density * 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGrid(canvas)
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

}