package com.simpletempco.simpletemp.ui.custom.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.util.ContextUtils.getResColor

class DayBackgroundDrawable constructor(val context: Context) : Drawable() {

    private var isSelected = false

    private var mFillColor: Int? = null
    private var selectedColor = Color.parseColor("#2B5D82")
    private var mStrokeColor = Color.parseColor("#2B5D82")

    private val fillPaint = Paint()
    private val strokePaint = Paint()
    private val selectedPaint = Paint()

    init {

        mStrokeColor = context.getResColor(R.color.colorDayStroke)

        fillPaint.apply {
            style = Paint.Style.FILL
            color = mFillColor ?: Color.TRANSPARENT
            isAntiAlias = true
        }

        selectedPaint.apply {
            style = Paint.Style.FILL
            color = selectedColor
            isAntiAlias = true
        }

        strokePaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 5f
            strokeJoin = Paint.Join.ROUND
            color = mStrokeColor
            isAntiAlias = true
        }
    }

    override fun draw(canvas: Canvas) {

        val centerX = bounds.centerX().toFloat()
        val centerY = bounds.centerY().toFloat()
        val radius = (bounds.width() / 2f)

        if (isSelected) {
            if (mFillColor != null) {
                canvas.drawCircle(centerX, centerY, radius, fillPaint)
                canvas.drawCircle(centerX, centerY, radius - 2.5f, strokePaint)
            } else {
                canvas.drawCircle(centerX, centerY, radius, selectedPaint)
            }
        } else {
            if (mFillColor != null) {
                canvas.drawCircle(centerX, centerY, radius, fillPaint)
            }
        }
    }

    fun setColor(color: Int?) {
        mFillColor = color
        fillPaint.color = mFillColor ?: Color.TRANSPARENT
        invalidateSelf()
    }

    fun setStrokeColor(color: Int) {
        mStrokeColor = color
        strokePaint.color = mFillColor ?: Color.TRANSPARENT
        invalidateSelf()
    }

    fun isSelected(selected: Boolean) {
        this.isSelected = selected
        invalidateSelf()
    }

    override fun setAlpha(alpha: Int) {

    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

}