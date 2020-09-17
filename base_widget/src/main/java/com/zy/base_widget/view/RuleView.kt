package com.zy.base_widget.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.zy.base_widget.R

open class RuleView : View {

    var mStyleableArray = intArrayOf(
            R.styleable.RuleView_ruleBackgroundColor,
            R.styleable.RuleView_ruleOrientation,
            R.styleable.RuleView_ruleScaleColor)
    var mRuleBackgroundColor = 0
    var mRuleOrientation = 0
    var mRuleScaleColor = 0
    var mRulePaintColor = 0
    var mMaxWidth = 0.0f
    var mMaxHeight = 0.0f
    var mMinWidth = 0.0f
    var mMinHeight = 0.0f
    var mMinScale: Float = 0.0f
    var mMaxScale: Float = 20.0f
    var mScaleStrokePx: Float = 3.0f //每个刻度的
    var mWholeScaleStrokePx: Float = 6.0f
    var mTextStrokePx:Float = 2.0f
    var mSectionValue: Int = 1 //每个区块 如： 1cm
    var mSectionPx: Int = 10   //区间像素 如：10px
    var mPaint: Paint = Paint()


//    @Retention(AnnotationRetention.RUNTIME)
//    @Target(AnnotationTarget.FIELD)
//    annotation class RuleOrientation{
//        lateinit var fun values():Int
//    }

    constructor(context: Context) : super(context) {
        loadAttributeSet(context.obtainStyledAttributes(mStyleableArray))
    }

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet) {
        loadAttributeSet(context.obtainStyledAttributes(attrSet, mStyleableArray))
    }

    fun loadAttributeSet(typedArray: TypedArray) {
        mRuleBackgroundColor = typedArray.getColor(R.styleable.RuleView_ruleBackgroundColor, 0)
        mRuleOrientation = typedArray.getInt(R.styleable.RuleView_ruleOrientation, 0)
        mRuleScaleColor = typedArray.getColor(R.styleable.RuleView_ruleScaleColor, 0)
        mRulePaintColor = typedArray.getColor(R.styleable.RuleView_ruleScaleColor, 0)
        typedArray.recycle()
    }


    fun tranformPaint() {
        mPaint.strokeWidth = mScaleStrokePx
        mPaint.color = mRulePaintColor
    }

    fun tranformTheWholePaint() {
        mPaint.strokeWidth = mWholeScaleStrokePx
        mPaint.color = mRulePaintColor
    }

    fun tranformTextPaint(){
        mPaint.strokeWidth = mTextStrokePx
        mPaint.color = mRulePaintColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        when (mRuleOrientation) {
            0 -> {
                if (widthMode == MeasureSpec.UNSPECIFIED) return
                if (widthMode == MeasureSpec.AT_MOST)
                    widthSize = getCountPoint() * mSectionPx
            }
            1 -> {
                if (heightMode == MeasureSpec.UNSPECIFIED) return
                if (heightMode == MeasureSpec.AT_MOST)
                    heightSize = getCountPoint() * mSectionPx
            }
        }
        setMeasuredDimension(widthSize, heightSize)
    }


    fun getTheWholeScale(currentScale: Int): Boolean {
        return currentScale % 100 == 0
    }

    fun getCountPoint() = Math.round((mMaxScale - mMinScale) / mSectionValue + 1)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        when (mRuleOrientation) {
            0 -> drawHor(canvas)
            1 -> drawVer()
        }
    }


    fun drawHor(canvas: Canvas?) {

        


    }


    fun drawVer() {

    }


    fun drawRule(canvas: Canvas?) {

        if (canvas == null) return
        var scale = 0
        while (scale < getCountPoint()) {

            if (getTheWholeScale(scale)) {
                tranformTheWholePaint()


            }else{
                tranformPaint()


            }
            tranformTextPaint()



            scale += mSectionValue
        }


    }


}