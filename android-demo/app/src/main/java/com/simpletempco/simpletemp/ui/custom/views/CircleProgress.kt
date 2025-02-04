package com.simpletempco.simpletemp.ui.custom.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.simpletempco.simpletemp.R

class CircleProgress @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mStroke = 25f
    private var startAngle = 30f
    private var progressAngle = 280f

    private var mPain: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = mStroke
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    init {

        // init attrs values
        initTypedArray(attrs)
    }

    private fun initTypedArray(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgress)
        try {

            gradiantColors(
                typedArray.getColor(R.styleable.CircleProgress_startColor, Color.GREEN),
                typedArray.getColor(R.styleable.CircleProgress_endColor, Color.GREEN)
            )

            startAngle(typedArray.getFloat(R.styleable.CircleProgress_startAngle, startAngle))

            stroke(typedArray.getFloat(R.styleable.CircleProgress_stroke, mStroke))

            percent(typedArray.getInt(R.styleable.CircleProgress_percent, 0))

        } finally {
            typedArray.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initColors()
    }

    private fun initColors() {

        mPain.shader = LinearGradient(
            0f, 0f, width.toFloat(), height.toFloat(),
            intArrayOf(Color.parseColor("#2B5D82"), Color.parseColor("#51B1B9")),
            null,
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // draw arc like circle
        canvas?.drawArc(
            mStroke / 2,
            mStroke / 2,
            width.toFloat() - (mStroke / 2),
            height.toFloat() - (mStroke / 2),
            startAngle,
            progressAngle,
            false,
            mPain
        )

    }

    fun stroke(stroke: Float) {
        mPain.strokeWidth = stroke
        mStroke = stroke
        invalidate()
    }

    fun gradiantColors(startColor: Int, endColor: Int) {
        mPain.shader = LinearGradient(
            0f, 0f, width.toFloat(), height.toFloat(),
            intArrayOf(endColor, startColor),
            null,
            Shader.TileMode.CLAMP
        )
        invalidate()
    }

    fun startAngle(startAngle: Float) {
        this.startAngle = startAngle
        invalidate()
    }

    fun progressAngle(progressAngle: Float) {
        this.progressAngle = progressAngle
        invalidate()
    }

    fun percent(percent: Int) {
        progressAngle(percent * 3.6f)
    }

}