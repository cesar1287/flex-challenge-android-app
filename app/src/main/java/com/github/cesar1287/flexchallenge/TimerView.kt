package com.github.cesar1287.flexchallenge

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.github.cesar1287.flexchallenge.Constants.TimerView.ARC_START_ANGLE
import com.github.cesar1287.flexchallenge.Constants.TimerView.THICKNESS_SCALE
import java.util.concurrent.TimeUnit

class TimerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null

    private var mCircleOuterBounds: RectF? = null
    private var mCircleInnerBounds: RectF? = null

    private var mCirclePaint: Paint? = null
    private var mCircleGrayPaint: Paint? = null
    private var mEraserPaint: Paint? = null

    private var mCircleSweepAngle = 0f

    private var mTimerAnimator: ValueAnimator? = null

    init {
        var circleColor: Int = Color.RED

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimerView)
            circleColor = typedArray.getColor(R.styleable.TimerView_circleColor, circleColor)
            typedArray.recycle()
        }

        mCirclePaint = Paint()
        mCirclePaint?.isAntiAlias = true
        mCirclePaint?.color = circleColor

        mCircleGrayPaint = Paint()
        mCircleGrayPaint?.isAntiAlias = true
        mCircleGrayPaint?.color = Color.GRAY

        mEraserPaint = Paint()
        mEraserPaint?.isAntiAlias = true
        mEraserPaint?.color = Color.TRANSPARENT
        mEraserPaint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        if (width != oldWidth || height != oldHeight) {
            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            mBitmap?.let {
                it.eraseColor(Color.TRANSPARENT)
                mCanvas = Canvas(it)
            }
        }
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        updateBounds()
    }

    override fun onDraw(canvas: Canvas) {
        mCanvas?.drawColor(0, PorterDuff.Mode.CLEAR)
        if (mCircleSweepAngle > 0f) {
            mCircleOuterBounds?.let { rectFOuter ->
                mCircleInnerBounds?.let { rectFInner ->
                    mCirclePaint?.let { paint ->
                        mCircleGrayPaint?.let { paintGray ->
                            mEraserPaint?.let { eraserPaint ->
                                mCanvas?.drawArc(
                                    rectFOuter,
                                    ARC_START_ANGLE,
                                    360f,
                                    true,
                                    paintGray
                                )

                                mCanvas?.drawArc(
                                    rectFOuter,
                                    ARC_START_ANGLE,
                                    mCircleSweepAngle,
                                    true,
                                    paint
                                )

                                mCanvas?.drawOval(rectFInner, eraserPaint)
                            }
                        }
                    }
                }
            }
        }

        mBitmap?.let { canvas.drawBitmap(it, 0F, 0F, null) }
    }

    fun start(seconds: Long) {
        stop()
        mTimerAnimator = ValueAnimator.ofFloat(0f, 1f)
        mTimerAnimator?.duration = TimeUnit.SECONDS.toMillis(seconds)
        mTimerAnimator?.interpolator = LinearInterpolator()
        mTimerAnimator?.addUpdateListener { animation ->
            drawProgress(
                animation.animatedValue as Float
            )
        }
        mTimerAnimator?.start()
    }

    private fun stop() {
        mTimerAnimator?.let {
            if (it.isRunning) {
                it.cancel()
                mTimerAnimator = null
                drawProgress(0f)
            }
        }
    }

    private fun drawProgress(progress: Float) {
        mCircleSweepAngle = 360 * progress
        invalidate()
    }

    private fun updateBounds() {
        val thickness = width * THICKNESS_SCALE
        mCircleOuterBounds = RectF(0F, 0F, width.toFloat(), height.toFloat())
        mCircleInnerBounds = RectF(
            (mCircleOuterBounds?.left ?: 0f) + thickness,
            (mCircleOuterBounds?.top ?: 0f) + thickness,
            (mCircleOuterBounds?.right ?: 0f) - thickness,
            (mCircleOuterBounds?.bottom ?: 0f) - thickness
        )
        invalidate()
    }

    fun setup() {
        drawProgress(0.0001f)
    }
}