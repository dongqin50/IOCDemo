package com.conagra.hardware.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.conagra.R;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.utils.DensityUtil;
import com.conagra.mvp.utils.LogMessage;


/**
 * Created by yedongqin on 2016/10/26.
 */

public class RulerView extends View {

    private int width;
    private int height;

    private float lineMax;
    private float lineMiddle;
    private float lineMin;
    private boolean isShowUp = true;//向上显示

    private int minScale;
    private int maxScale;
    private float marginX;
    private int startX;
    private int currentScale;
    private int oldScale;
    private float marginY;
    private Scroller mScroller;
    protected int mScrollLastX;
    protected int mCountScale = 0; //滑动的总刻度
    private VelocityTracker mVelocityTracker;
    private int mScaledMinimumFlingVelocity;
    private int mScaledMaximumFlingVelocity;
    private int minWidth;
    private Context context;
    private boolean isScroller;
    private int minValue;
    private BaseListener.Observable111<String,Integer> mListener;

    public void setShowUp(boolean showUp) {
        isShowUp = showUp;
        postInvalidate();
    }

    public RulerView(Context context) {
        this(context,null);
    }

    public RulerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mScroller = new Scroller(context);
        mScaledMinimumFlingVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        mScaledMaximumFlingVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        mVelocityTracker = VelocityTracker.obtain();
    }

    public void setmListener(BaseListener.Observable111<String, Integer> mListener) {
        this.mListener = mListener;
    }

    public int getCurrentScale() {
        return currentScale;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        if(widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else{
            width = getMeasuredWidth();
        }
        if(heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else{
            height = getMeasuredHeight();
        }
        setMeasuredDimension(width,height);
        maxScale = 300;
        marginX = (width - 100 - minWidth) / 60;
        lineMiddle = height/3 - height/24 + height / 20 ;
        lineMax = lineMiddle + height/8;
        lineMin = lineMiddle - height/12;
        minScale = 40;
        minValue = 40;
        marginY = 20;
//        oldScale = 40;
        minWidth = 40;

    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mListener != null){
            mListener.onAction1(l,t,oldl,oldt);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(isShowUp){
            drawRulerResever(canvas);
        }else {
            drawRuler(canvas);
        }

        if(isScroller){
            if(oldScale == 0){
                int x = getScale(currentScale);
                scrollTo(x,0);
            }else {
                int x = getScale(currentScale) - getScale(oldScale);
                LogMessage.doLogMessage("aab","xx : " + x + " currentScale : " + currentScale+ " oldScale : " + oldScale );
                scrollBy(x,0);
            }
            isScroller = false;
        }
    }

    private int getScale(int data){
        if(data == 0){
            return 0;
        }
        return (int) ((data-minScale) * marginX) - width/2 + minWidth;
    }

    public void setCurrentScale(int currentScale) {
//        if(this.currentScale != 0)
        this.oldScale = this.currentScale;

        this.currentScale = currentScale;
        isScroller = true;
        postInvalidate();
    }


    public void doScroller(int dataX){
        scrollBy(dataX, 0);
        invalidate();
        if (mListener != null) {
            int finalX = getScrollX() - minWidth + width / 2;
            //滑动的刻度
            int tmpCountScale = (int) Math.rint((double) finalX / (double) marginX); //四舍五入取整
            currentScale = tmpCountScale + minScale;
            mListener.onAction(currentScale + "");
        }
    }
    public void doScroller(int x,int y){
        scrollTo(x,y);
        invalidate();
        if (mListener != null) {
            int finalX = getScrollX() - minWidth + width / 2;
            //滑动的刻度
            int tmpCountScale = (int) Math.rint((double) finalX / (double) marginX); //四舍五入取整
            currentScale = tmpCountScale + minScale;
            mListener.onAction(currentScale + "");
        }
    }

    public void drawRuler(Canvas canvas){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(getResources().getColor(R.color.color_text1));
        paint.setStrokeWidth(3);

        paint.setTextSize(DensityUtil.dp2px(context,20));

            for(int i = 0; i <= (maxScale - minScale); i++){
                float y = 0;
                float x = i * marginX + minWidth;
                if(i % 10 == 0){
                    y += lineMax;
                    String text = (minScale + i) + "";
                    paint.setColor(getResources().getColor(R.color.ruler_color1));
                    float length = paint.measureText(text);
                    canvas.drawText(text,x - (length/2),y + marginY + 30,paint);
                    paint.setColor(getResources().getColor(R.color.ruler_color2));
                }else if(i % 5 == 0){
                    paint.setColor(getResources().getColor(R.color.ruler_color3));
                    y += lineMiddle;
                }else {
                    paint.setColor(getResources().getColor(R.color.ruler_color4));
                    y += lineMin;
                }
                canvas.drawLine(x,0,x,y,paint);
            }
    }


    public void drawRulerResever(Canvas canvas){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(getResources().getColor(R.color.color_text1));
        paint.setStrokeWidth(3);
        paint.setTextSize(DensityUtil.dp2px(context,20));

            for(int i = 0; i <= (maxScale - minScale); i++){
                float y = height;
                float x = i * marginX + minWidth;
                if(i % 10 == 0){
                    paint.setColor(getResources().getColor(R.color.ruler_color1));
                    y -= lineMax;
                    String text = (minScale + i) + "";
                    float length = paint.measureText(text);
                    canvas.drawText(text,x - (length/2),y - marginY,paint);
                    paint.setColor(getResources().getColor(R.color.ruler_color2));
                }else if(i % 5 == 0){
                    y -= lineMiddle;
                    paint.setColor(getResources().getColor(R.color.ruler_color3));
                }else {
                    paint.setColor(getResources().getColor(R.color.ruler_color4));
                    y -= lineMin;
                }
                canvas.drawLine(x,height,x,y,paint);
            }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            // 通过重绘来不断调用computeScroll
            postInvalidate();
            //需要减去一开始的偏移距离
            int finalX = mScroller.getCurrX() - minWidth + width / 2;
            //滑动的刻度 (除以刻度间隔 算出滑动了多少格)
            int tmpCountScale = (int) Math.rint((double) finalX / (double) marginX); //四舍五入取整
            //边界判断
            if(tmpCountScale < 0){
                tmpCountScale = 0;
            }
            currentScale = tmpCountScale + minScale;
            if (mListener != null) {
                mListener.onAction(currentScale + "");
            }


        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mVelocityTracker.addMovement(event);

        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mScroller != null && !mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mScrollLastX = x;
                return true;
            case MotionEvent.ACTION_MOVE:
                mCountScale = getScrollX();
                int dataX = mScrollLastX - x;
                doScroller(dataX);
                mScrollLastX = x;
                return false;
            case MotionEvent.ACTION_UP:
                //使用mVelocityTracker来计算速度，
                //计算当前速度， 1代表px/毫秒, 1000代表px/秒,
                mVelocityTracker.computeCurrentVelocity(1000, mScaledMaximumFlingVelocity);
                int xVelocity = (int) mVelocityTracker.getXVelocity();
                mVelocityTracker.clear();
                doVelocityTracker(xVelocity,0,0);
                return false;
        }
        return true;
    }

    public void doVelocityTracker(int xVelocity,int min,int max){

        if(min == 0){
            min = (- width/2) + minWidth;
        }else {
            min = (int) ((min - minScale) * marginX) - width/2 + minWidth;
        }

        if(max == 0){
            max = (int) ((maxScale - minScale) * marginX - width/2 + minWidth);
        }else {
            max = (int) ((max - minScale) * marginX) - width/2 + minWidth;
        }
//                int max = 20000;
        if (Math.abs(xVelocity) > mScaledMinimumFlingVelocity) {
            mScroller.fling(getScrollX(), 0, -xVelocity, 0, min, max, 0, 0);//根据坐标轴正方向问题，这里需要加上-号
        } else {
            if (mCountScale < min) {
                mCountScale = min;
            } else if (mCountScale > max) {
                mCountScale = max;
            }
            mScroller.setFinalX(mCountScale); //纠正指针位置
        }
        postInvalidate();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
