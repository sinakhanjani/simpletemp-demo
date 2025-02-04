package com.simpletempco.simpletemp.util

import android.graphics.*
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.CheckResult
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.simpletempco.simpletemp.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart


/*

*/
/**
 * Load model into ImageView as a circle image with borderSize (optional) using Glide
 *
 * @param model - Any object supported by Glide (Uri, File, Bitmap, String, resource id as Int, ByteArray, and Drawable)
 * @param borderSize - The border size in pixel
 * @param borderColor - The border color
 */

fun <T> ImageView.loadCircularImage(
    model: T,
    @DrawableRes placeHolder: Int = R.drawable.ic_default_account,
    borderSize: Float = 0F,
    borderColor: Int = Color.WHITE
) {
    Glide.with(context)
        .asBitmap()
        .load(model)
        .placeholder(placeHolder)
        .apply(RequestOptions.circleCropTransform())
        .into(object : BitmapImageViewTarget(this) {
            override fun setResource(resource: Bitmap?) {
                setImageDrawable(
                    resource?.run {
                        RoundedBitmapDrawableFactory.create(
                            resources,
                            if (borderSize > 0) {
                                createBitmapWithBorder(borderSize, borderColor)
                            } else {
                                this
                            }
                        ).apply {
                            isCircular = true
                        }
                    }
                )
            }
        })
}

/**
 * Create a new bordered bitmap with the specified borderSize and borderColor
 *
 * @param borderSize - The border size in pixel
 * @param borderColor - The border color
 * @return A new bordered bitmap with the specified borderSize and borderColor
 */

private fun Bitmap.createBitmapWithBorder(borderSize: Float, borderColor: Int): Bitmap {
    val borderOffset = (borderSize * 2).toInt()
    val halfWidth = width / 2
    val halfHeight = height / 2
    val circleRadius = Math.min(halfWidth, halfHeight).toFloat()
    val newBitmap = Bitmap.createBitmap(
        width + borderOffset,
        height + borderOffset,
        Bitmap.Config.ARGB_8888
    )

    // Center coordinates of the image
    val centerX = halfWidth + borderSize
    val centerY = halfHeight + borderSize

    val paint = Paint()
    val canvas = Canvas(newBitmap).apply {
        // Set transparent initial area
        drawARGB(0, 0, 0, 0)
    }

    // Draw the transparent initial area
    paint.isAntiAlias = true
    paint.style = Paint.Style.FILL
    canvas.drawCircle(centerX, centerY, circleRadius, paint)

    // Draw the image
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, borderSize, borderSize, paint)

    // Draw the createBitmapWithBorder
    paint.xfermode = null
    paint.style = Paint.Style.STROKE
    paint.color = borderColor
    paint.strokeWidth = borderSize
    canvas.drawCircle(centerX, centerY, circleRadius, paint)
    return newBitmap
}

fun showView(v: View) {
    if (v.isShown) return
    with(v) {
        visibility = VISIBLE
        animate()
            .alpha(1f)
            .translationY(0F)
            .duration = 500
    }
}

fun hideView(v: View, fromTop: Boolean = false) {
    with(v) {
        if (visibility == VISIBLE && alpha == 1f) {
            animate()
                .alpha(0f)
                .translationY(if (fromTop) -height.toFloat() else height.toFloat())
                .withEndAction { visibility = GONE }
                .duration = 500
        }
    }
}

private fun checkMainThread() {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Expected to be called on the main thread but was " + Thread.currentThread().name
    }
}

fun MaterialButton.changeColor(color: Int) {
    this.background.setTintList(ContextCompat.getColorStateList(context, color))
}

fun MaterialTextView.changeColor(color: Int) {
    this.background.setTintList(ContextCompat.getColorStateList(context, color))
}

fun View.changeColor(color: Int) {
    this.background.setTintList(ContextCompat.getColorStateList(context, color))
}

@ExperimentalCoroutinesApi
@CheckResult
fun EditText.textChanges(): Flow<CharSequence?> {
    return callbackFlow {
        checkMainThread()

        val listener = doOnTextChanged { text, _, _, _ -> trySend(text) }
        awaitClose { removeTextChangedListener(listener) }
    }.onStart { emit(text) }
}

fun View.collapse() {
    val initialHeight: Int = measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                visibility = GONE
            } else {
                layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    a.duration = (initialHeight / context.resources.displayMetrics.density).toInt().toLong()
    startAnimation(a)
}

fun View.expand() {

    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val targetHeight: Int = measuredHeight

//        measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//        val targetHeight: Int = measuredHeight

    layoutParams.height = 1
    visibility = VISIBLE

    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            layoutParams.height = if (interpolatedTime == 1f)
//                    LinearLayout.LayoutParams.WRAP_CONTENT
                (targetHeight * interpolatedTime).toInt()
            else
                (targetHeight * interpolatedTime).toInt()
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    a.duration = (targetHeight / context.resources.displayMetrics.density).toInt().toLong()
    startAnimation(a)
}

fun String.changeColorSomeText(
    color: Int,
    vararg paths: String
): Spannable {
    val spannable: Spannable = SpannableString(this)
    for (path in paths) {
        val indexOfPath = spannable.toString().indexOf(" $path")
        if (indexOfPath == -1) {
            continue
        }
        spannable.setSpan(
            ForegroundColorSpan(color),
            indexOfPath,
            indexOfPath + path.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            indexOfPath,
            indexOfPath + path.length + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return spannable
}