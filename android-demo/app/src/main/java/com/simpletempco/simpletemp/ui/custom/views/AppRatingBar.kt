package com.simpletempco.simpletemp.ui.custom.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.util.AppRatingBarChangeListener


class AppRatingBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var imageFill: Bitmap
    private lateinit var imageEmpty: Bitmap

    private var iconCount = 5
    private var progress = 0
    private var iconSize = 20f
    private var ratingMode = 0
    private var betweenSpace = 0f

    private var ability = false
    private val mPaint = Paint()

    private var callback: AppRatingBarChangeListener? = null

    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppRatingBar)
        try {
            setFilledIcon(
                typedArray.getResourceId(
                    R.styleable.AppRatingBar_filledStar,
                    R.mipmap.ic_launcher
                )
            )
            setEmptyIcon(
                typedArray.getResourceId(
                    R.styleable.AppRatingBar_emptyStar,
                    R.mipmap.ic_launcher
                )
            )

            progress = typedArray.getInt(R.styleable.AppRatingBar_rating, 0)
            iconCount = typedArray.getInt(R.styleable.AppRatingBar_count, 4)
            ability = typedArray.getBoolean(R.styleable.AppRatingBar_ability, false)
            ratingMode = typedArray.getInt(R.styleable.AppRatingBar_ratingMode, 0)
            betweenSpace = typedArray.getDimension(R.styleable.AppRatingBar_betweenSpace, 0f)
        } finally {
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val mHeight = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(
            ((mHeight * iconCount) + (betweenSpace * (iconCount - 1))).toInt(),
            mHeight
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        initIconSize()
        for (i in 0 until iconCount) {
            canvas?.drawBitmap(
                getIcon(i),
                (i * (iconSize + betweenSpace)),
                0F,
                mPaint
            )
        }
    }

    private fun setFilledIcon(res: Int) {
        AppCompatResources.getDrawable(context, res)?.let {
            imageFill = drawableToBitmap(it)
        }
    }

    private fun setEmptyIcon(res: Int) {
        AppCompatResources.getDrawable(context, res)?.let {
            imageEmpty = drawableToBitmap(it)
        }
    }

    private fun getIcon(position: Int): Bitmap {
        return Bitmap.createScaledBitmap(
            if (position < progress) imageFill else imageEmpty, iconSize.toInt(),
            iconSize.toInt(),
            false
        )
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width - 5, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun initIconSize() {
        iconSize = height.toFloat()
    }

    fun rating(rating: Int) {
        progress = rating
        callback?.onRatingChange(rating)
        invalidate()
    }

    fun getRating() = progress

    fun addRatingChangeListener(appRatingBarChangeListener: AppRatingBarChangeListener) {
        callback = appRatingBarChangeListener
    }

    /*private fun getDesiredSize(): Float {

        val h = height.toFloat()
        val w: Float = (width - (percentSpace * width)) / iconCount

        dashSpace = when {
            betweenSpace != 0f -> {
                betweenSpace
            }
            h < w -> {
                ((width - (iconCount * h)) / (iconCount - 1))
            }
            else -> {
                (percentSpace * width) / (iconCount - 1)
            }
        }
        return min(w, h)
    }*/

    private var touchDownPoint = PointF()

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!ability) return false
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDownPoint.set(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                if (ratingMode == 0)
                    handleTouchPost(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                if (ratingMode == 0) {
                    //Click Happened
                    if (touchDownPoint.x == event.x &&
                        touchDownPoint.y == event.y
                    ) {
                        handleTouchPost(event.x, event.y)
                    }
                }
            }
        }
        return true
    }


    private fun handleTouchPost(x: Float, y: Float) {
        for (i in 0 until iconCount) {
            val stepArea = RectF(
                (i * (width / iconCount)).toFloat(),
                0F,
                (i * (width / iconCount)) + (width / iconCount).toFloat(),
                iconSize
            )

            if (x < ((width / iconCount) / 3)) {
                rating(0)
            } else if (stepArea.contains(x, y)) {
                rating(i + 1)
            }
        }
    }

}