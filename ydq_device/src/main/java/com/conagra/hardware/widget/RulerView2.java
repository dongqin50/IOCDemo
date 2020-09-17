package com.conagra.hardware.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.conagra.R;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.utils.DensityUtil;


/**
 * Created by yedongqin on 2016/10/28.
 */

public class RulerView2 extends View {

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
    private float currentScale;
    private float oldScale;
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

    private BaseListener.Observable<String> mListener;

    public void setShowUp(boolean showUp) {
        isShowUp = showUp;
        postInvalidate();
    }

    public RulerView2(Context context) {
        this(context,null);
    }

    public RulerView2(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RulerView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mScroller = new Scroller(context);
        mScaledMinimumFlingVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        mScaledMaximumFlingVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        mVelocityTracker = VelocityTracker.obtain();
    }

    public void setmListener(BaseListener.Observable<String> mListener) {
        this.mListener = mListener;
    }

    public double getCurrentScale() {
        return currentScale;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
        maxScale = 42;
        marginX = (width - 100) / 60;
        lineMiddle = height/3 + height/12 ;
        lineMax = lineMiddle + height/6;
        lineMin = lineMiddle - height/6;
        minScale = 35;
        marginY = 40;
        minWidth = width/2;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        if(isShowUp){
//            drawRulerResever(canvas);
//        }else {
//            drawRuler(canvas);
//        }
        drawRuler(canvas);
        if(isScroller){
            if(oldScale > 0){
                int x = getData(currentScale) - getData(oldScale);
                scrollBy(x,0);

            }else {
                int x = getData(currentScale);
                scrollTo(x,0);
            }
            isScroller = false;
        }
    }

    private int getData(float data){
        return (int) ((data - minScale) * marginX * 10) - width/2 + minWidth;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawRuler(Canvas canvas){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(getResources().getColor(R.color.color_text1));
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        float xx = (maxScale - minScale + 1) * 10 * marginX + minWidth;
        canvas.drawArc(new RectF(minWidth - 15 * marginX,0,height + minWidth- 15 * marginX,height),90,180,false,paint);
//        canvas.drawArc();

        canvas.drawArc(new RectF(xx-(height+ (marginX/2)),0,xx-(marginX/2),height),270,180,false,paint);
        canvas.drawLine(height/2 + minWidth- 15 * marginX,0,xx-((height+ marginX)/2),0,paint);
        canvas.drawLine(height/2 + minWidth- 15 * marginX,height,xx-((height+ marginX)/2),height,paint);

        paint.setTextSize(DensityUtil.dp2px(context,14));
        paint.setColor(getResources().getColor(R.color.ruler_color4));
        canvas.drawLine(minWidth - marginX,(height - lineMin - 20)/2,minWidth - marginX,lineMin + (height - lineMin - 20)/2,paint);
        canvas.drawLine(minWidth - 2 * marginX,(height - lineMin - 20)/2,minWidth - 2 * marginX,lineMin + (height - lineMin - 20)/2,paint);
        canvas.drawLine(minWidth - 3 * marginX,(height - lineMin - 20)/2,minWidth - 3 * marginX,lineMin + (height - lineMin - 20)/2,paint);
        canvas.drawLine(minWidth - 4 * marginX,(height - lineMin - 20)/2,minWidth - 4 * marginX,lineMin + (height - lineMin - 20)/2,paint);


        for(int i = 0; i <= (maxScale - minScale) * 10; i++){
            float y = 0;
            float x = i * marginX + minWidth;
            if(i % 10 == 0){
                y += lineMax;
                String text = ((i/10) + minScale) + "";
                paint.setColor(getResources().getColor(R.color.ruler_color1));
                float length = paint.measureText(text);
                canvas.drawText(text,x - (length/2),y + marginY + (height - y - 20)/2 - 10,paint);
                paint.setColor(getResources().getColor(R.color.ruler_color2));
            }else if(i % 5 == 0){
                paint.setColor(getResources().getColor(R.color.ruler_color3));
                y += lineMiddle;
            }else {
                paint.setColor(getResources().getColor(R.color.ruler_color4));
                y += lineMin;
            }
            canvas.drawLine(x,(height - y - 20)/2,x,y + (height - y - 20)/2,paint);
        }
        paint.setStrokeWidth(DensityUtil.dp2px(context,6));
        paint.setColor(getResources().getColor(R.color.bloodsugarview_color3));
        canvas.drawLine(minWidth - 7 * marginX,height/2 - 10,xx-10 * marginX,height/2 - 10,paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(minWidth - 7 * marginX,height/2 - 10,18,paint);
//        paint.setColor(getResources().getColor(R.color.colorPick));
//        canvas.drawLine(minWidth,height,150 * marginX + minWidth,height,paint);
    }

    public void setCurrentScale(float currentScale) {
        oldScale = this.currentScale;
        this.currentScale = currentScale;
        isScroller = true;
        postInvalidate();
    }

    public void doScroller(int dataX){
        scrollBy(dataX, 0);
        invalidate();
        if (mListener != null) {
            float finalX = getScrollX() - minWidth + width / 2;
            //滑动的刻度
            float tmpCountScale =  finalX /  marginX; //四舍五入取整
            currentScale = tmpCountScale/10 + minScale;
            if (mListener != null) {
                mListener.onAction(String.format("%.1f",currentScale));
            }
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
            float finalX = mScroller.getCurrX() - minWidth + width / 2;
            //滑动的刻度 (除以刻度间隔 算出滑动了多少格)
            float tmpCountScale = finalX /  marginX; //四舍五入取整
            //边界判断
            if(tmpCountScale < 0){
                tmpCountScale = 0;
            }
            currentScale = tmpCountScale/10 + minScale;
            if (mListener != null) {
                mListener.onAction(String.format("%.1f",currentScale));
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
            max = (int) ((maxScale - minScale) * 10 * marginX - width/2 + minWidth);
        }else {
            max = (int) ((max - minScale) * 10 * marginX) - width/2 + minWidth;
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
