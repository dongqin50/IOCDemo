package com.conagra.hardware.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.conagra.R;
import com.conagra.mvp.utils.CommonUtils;


/**
 * Created by yedongqin on 16/9/12.
 */
public class FetalHeartView extends View {

    /**
     * 折线笔
     */
    private Paint mLinePaint;
    private Paint mTextPaint;
    private Paint mPointPaint;
    private int marginTop;
    private float maxTime = 220;
//    private int maxScale = 20000;
    /**
     * 表格笔
     */
    private Paint mFormPaint;


    /**
     * view宽,高
     */
    private float width;
    private float height;

    /**
     * 当前宽度
     */
    private float currentWidth;
    private long currentTime;

    private int startX; //初始值
    private int endX; //

    private int startY;
    private int endY;
    private boolean isStart;


    /**
     * 行间距,列间距
     */
    private float rowMargin;
    private float colMargin;
    private StringBuilder mBuilder;
//    private StringBuilder mGSBuilder;

    private Scroller mScroller;

    private VelocityTracker mVelocityTracker;
    private float mScaledMinVelocity;
    private float mScaledMaxVelocity;

    private long startTime;

    public FetalHeartView(Context context) {
        super(context);
        doInit(context);
    }

    public boolean isStart() {
        return isStart;
    }

    public FetalHeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        doInit(context);
    }

    public FetalHeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        doInit(context);
    }


    public void doInit(Context context) {

        mLinePaint = new Paint();
        mLinePaint.setColor(getResources().getColor(android.R.color.holo_green_dark));
        mLinePaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(getResources().getColor(android.R.color.black));

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setColor(getResources().getColor(R.color.colorMain));

        mFormPaint = new Paint();
        mFormPaint.setAntiAlias(true);
        mFormPaint.setColor(getResources().getColor(R.color.colorMain));

        mBuilder = new StringBuilder();
//        mGSBuilder = new StringBuilder();
        mScroller = new Scroller(context);
        mVelocityTracker = VelocityTracker.obtain();
        mScaledMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mScaledMinVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        //判断是否执行完毕
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());

            postInvalidate();
            int finalX = mScroller.getCurrX() - startX ;
            int tmpCount = Math.round(finalX / rowMargin);
            if(tmpCount < 0){
                tmpCount = 0;
            }
        }
    }

    /**
     * 开始
     */
    public void doStart(){
        startTime = System.currentTimeMillis();
        isStart = true;
    }

    public void doStop(){
        isStart = false;
        if(startTime > 0 && mBuilder.length() > 0){
            mBuilder.delete(0,mBuilder.length());
        }
//        if(startTime > 0 && mGSBuilder.length() > 0){
//            mGSBuilder.delete(0,mGSBuilder.length());
//        }

        startTime = 0;
        postInvalidate();
    }

    /**
     * 添加数值
     * @param value
     */
    public void addValue(String value,String gsValue){

        if(startTime == 0)
            return;
        int o = Integer.parseInt(value);
        int a = Integer.parseInt(gsValue);
//        endTime = System.currentTimeMillis() + 1000*60*2 + 1000*20;
        if(o < 30){
            value = 30.0 + "";
        }else if(o > 240){
            value = 240.0 + "";
        }

        if(a > 100){
            gsValue = 100 + "";
        }else if(a < 0){
            gsValue = 0 + "";
        }

//        currentTime = (System.currentTimeMillis()-startTime)/1000 + 60 *3 + 30;
        currentTime = (System.currentTimeMillis()-startTime)/1000;
        mBuilder.append(currentTime + ":" + value + ";");
//        mGSBuilder.append(currentTime + ":" + gsValue + ";");
        /**
         * 重新绘制
         */
        postInvalidate();
    }

    public String[] getValues(StringBuilder builder){
        String value = builder.toString();
        if(!TextUtils.isEmpty(value) && value.contains(";")){
            return value.substring(0,value.length()-1).split(";");
        }else{
            return null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        width = getMeasuredWidth();
//        height = getMeasuredHeight();
//        currentWidth = getMeasuredWidth();
//        colMargin = 120;
//        rowMargin = 60;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthModel = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);

        if(widthModel == MeasureSpec.EXACTLY){
            width = widthSize;
        }else{
            width = getMeasuredWidth();
        }

        if(heightModel == MeasureSpec.EXACTLY){
            height = heightSize;
        }else {
            height = getMeasuredHeight();
        }

        colMargin = (width - 50)/11;
        rowMargin = height/27;
        endX = (int) width;
        startX = 0;
        startY = 0;
        endY = (int) height;
        setMeasuredDimension((int)width,(int)height);
        currentWidth = colMargin * 61 * 3 + 10;
    }
    /**
     * 绘制表格
     * @param canvas
     */
    private void doDrawForm(Canvas canvas){

        mFormPaint.setStrokeWidth(10.0f);
        mTextPaint.setStrokeWidth(28.0f);
        mPointPaint.setStrokeWidth(20.0f);
        mTextPaint.setTextSize(28);
        canvas.drawLine(0,0,currentWidth,0,mFormPaint);
//        canvas.drawText("胎心率/宫缩压",10,2* rowMargin,mTextPaint);
        canvas.drawText("胎心率",10,2* rowMargin,mTextPaint);
        mTextPaint.setStrokeWidth(22.0f);
        mTextPaint.setTextSize(22);
        mFormPaint.setStrokeWidth(1.0f);
        mFormPaint.setTextSize(20);
        for(int c = 0; c < (currentWidth-50)/colMargin; c++){
            float x = c * colMargin + 50 + startX;
            PointF startF = new PointF(x,rowMargin*3);
            PointF endF = new PointF(x,rowMargin * 24);

            canvas.drawLine(startF.x,startF.y,endF.x,endF.y,mFormPaint);

            PointF startF1 = new PointF(x,rowMargin*27);
            PointF endF1 = new PointF(x,rowMargin * 37);
            canvas.drawLine(startF1.x,startF1.y,endF1.x,endF1.y,mFormPaint);

            if(c % 3 == 0 && c != 0){
                String  value = Integer.valueOf(Math.round(c/3)).toString();
                canvas.drawText(value +"min",x-20,26 * rowMargin,mTextPaint);
//                LogMessage.doLogMessage(FetalHeartView.class.getSimpleName()," value : " + value + " ");
            }
        }
        mTextPaint.setStrokeWidth(20.0f);
        mTextPaint.setTextSize(20);
        for(int r = 3; r < 25; r++){
            float y = r * rowMargin;

            PointF startF = new PointF(0,y);
            PointF endF = new PointF(currentWidth,y);

            canvas.drawLine(startF.x,startF.y,endF.x,endF.y,mFormPaint);
            if(r % 3 == 0){
                String  value = Integer.valueOf(270-30 * Math.round(r/3)).toString();
                if(value.length() == 2){
                    canvas.drawText(value +"",20,y+5,mTextPaint);
                }else if(value.length() == 3){
                    canvas.drawText(value +"",10,y+5,mTextPaint);
                }

                canvas.drawCircle(50,y,5,mPointPaint);

//                LogMessage.doLogMessage(FetalHeartView.class.getSimpleName()," value : " + value + " ");
            }
        }

//        for(int r = 27; r < 38; r++){
//            float y = r * rowMargin;
//
//            PointF startF = new PointF(0,y);
//            PointF endF = new PointF(currentWidth,y);
//
//            canvas.drawLine(startF.x,startF.y,endF.x,endF.y,mFormPaint);
//            if(r % 2 == 1){
//                String  value = Integer.valueOf(360-20 * Math.round(r/2)).toString();
//                if(value.length() == 1){
//                    canvas.drawText(value +"",30,y,mTextPaint);
//                }else if(value.length() == 2){
//                    canvas.drawText(value +"",20,y,mTextPaint);
//                }else if(value.length() == 3){
//                    canvas.drawText(value +"",10,y,mTextPaint);
//                }
//                canvas.drawCircle(50,y,5,mPointPaint);
////                LogMessage.doLogMessage(FetalHeartView.class.getSimpleName()," value : " + value + " ");
//            }
//        }

        mFormPaint.setStrokeWidth(10.0f);
        canvas.drawLine(0,height,currentWidth,height,mFormPaint);
    }


    private void doDrawLine(Canvas canvas){
        mLinePaint.setStrokeWidth(4.0f);
        mLinePaint.setColor(getResources().getColor(R.color.colorPick));
        String[] values = getValues(mBuilder);
//        String[] gsyValues = getValues(mGSBuilder);
        if(values != null){

            if(values.length >1){
                int i = 0;
                while ( i + 1 < values.length){
                    String[] ss = values[i].split(":");  //起点
//                    String[] ss1 = gsyValues[i].split(":");  //起点
                    String[] es = values[++i].split(":");  //终点
                    PointF sf = getLocationPoint(ss[0],ss[1]);
                    PointF ef = getLocationPoint(es[0],es[1]);

                    canvas.drawLine(sf.x,sf.y,ef.x,ef.y,mLinePaint);

//                    String[] es1 = gsyValues[i].split(":");  //终点
//                    PointF sf1 = getLocationPoint1(ss1[0],ss1[1]);
//                    PointF ef1 = getLocationPoint1(es1[0],es1[1]);
//
//                    canvas.drawLine(sf1.x,sf1.y,ef1.x,ef1.y,mLinePaint);
                }

            }else{
//                String[] ss = values[0].split(":");  //起点
//                canvas.drawCircle(getRowValue(ss[0]),getColValue(ss[1]),2,mLinePaint);
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        doDrawForm(canvas);

        doDrawLine(canvas);

//        if(currentTime > maxTime){
//            currentWidth += 3 * colMargin;
//            maxTime += 60;
//            postInvalidate();
//            this.layout(Math.round(width - currentWidth),0,Math.round(2 * width - currentWidth),Math.round(height));
//            layout((int) (currentWidth-width),
//                    ActivityManageUtils.getMyApplication().headTop + 20,
//                    Math.round(getWidth()-(currentWidth - width)),
//                    Math.round(height+ActivityManageUtils.getMyApplication().headTop +20));
//        }
        postInvalidate();
    }

    /**
     * 获取当前位置
     * @param col
     * @param row
     * @return
     */
    private PointF getLocationPoint(@NonNull String row, @NonNull String col){

        PointF pointF = new PointF();
        float x= 0 ;
        float y = 0;

        float f = 0;
        if(CommonUtils.isNumber(row)){

            //将时间,ms转化成s
            float  num = Float.parseFloat(Long.valueOf(row).toString());
            f = num;
            x = num * colMargin /20 + 50;
        }

        if(CommonUtils.isNumber(col)){


            float num = Float.parseFloat(col);

            y = (270 - num)*rowMargin/10;
        }

        pointF.set(x,y);
//        if(f > maxTime){
//            currentWidth += colMargin;
//            maxTime += 20;
////            postInvalidate();
//            this.layout(Math.round(width - currentWidth),0,Math.round(2 * width - currentWidth),Math.round(height));
//        }
//        LogMessage.doLogMessage(FetalHeartView.class.getSimpleName(), " row = " + f + " col = " + col );
//        LogMessage.doLogMessage(FetalHeartView.class.getSimpleName(), " width  = " + width + " currentWidth = " + currentWidth );
//
//        LogMessage.doLogMessage(FetalHeartView.class.getSimpleName()," x = " + x + " y = " + y);
        return pointF;
    }
 /**
     * 获取当前位置
     * @param col
     * @param row
     * @return
     */
    private PointF getLocationPoint1(@NonNull String row,@NonNull String col){

        PointF pointF = new PointF();
        float x= 0 ;
        float y = 0;

        float f = 0;
        if(CommonUtils.isNumber(row)){

            //将时间,ms转化成s
            float  num = Float.parseFloat(Long.valueOf(row).toString());
            f = num;
            x = num * colMargin /20 + 50;
        }

        if(CommonUtils.isNumber(col)){


            float num = Float.parseFloat(col);

            y = (370 - num)*rowMargin/10;
        }

        pointF.set(x,y);
//        LogMessage.doLogMessage(FetalHeartView.class.getSimpleName(), " row = " + f + " col = " + col );
//        LogMessage.doLogMessage(FetalHeartView.class.getSimpleName(), " width  = " + width + " currentWidth = " + currentWidth );
//
//        LogMessage.doLogMessage(FetalHeartView.class.getSimpleName()," x = " + x + " y = " + y);
        return pointF;
    }

    private float getRowValue(float time){
       return time * colMargin/20 + 50;
    }
    float downX = 0;
    private float mScrollLastX = 0;
    private float mCountX = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int action = event.getAction();
        switch (action){

            case MotionEvent.ACTION_DOWN:

                if(mScroller != null && !mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                mScrollLastX = event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                mCountX = getScrollX();
                float moveX = mScrollLastX - event.getX();
//                if(mCountX < startX){
//                    mCountX = startX;
//                }else if(mCountX > currentWidth){
//                    mCountX = currentWidth;
//                }
                scrollBy((int) moveX,0);

                mScrollLastX = event.getX();
                invalidate();


                return true;
            case MotionEvent.ACTION_UP:
                //惯性滑动
                mVelocityTracker.computeCurrentVelocity(1000,mScaledMaxVelocity);
                int xVelocity = (int) mVelocityTracker.getXVelocity();
                mVelocityTracker.clear();
                //根据坐标轴正方向问题,这里需要加上 -
//                if(mCountX < startX){
//                    mCountX = startX;
//                }else if(mCountX > currentWidth){
//                    mCountX = currentWidth;
//                }
//                mScroller.setFinalX((int) mCountX);


                if(Math.abs(xVelocity) > mScaledMinVelocity){
                    int x = getScrollX();
                    if(x > currentWidth){
                        x = (int) currentWidth;
                    }else if(x < startX){
                        x = startX;
                    }
                    mScroller.fling(x,0,-xVelocity,0,startX, (int) currentWidth,0,0);
                }
                else {
                    if(mCountX < startX){
                        mCountX = startX;
                    }else if(mCountX + width > currentWidth){
                        mCountX = currentWidth;
                    }
                    mScroller.setFinalX((int) mCountX);
                }
                postInvalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    //    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        int action = event.getAction();
//        switch (action){
//            case MotionEvent.ACTION_DOWN:
//                downX = event.getX();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                moveX += event.getX() - downX;
//                LogMessage.doLogMessage(FetalHeartView.class.getSimpleName()," moveX = " + moveX);
//                if(moveX <= 0)
//                    layout(Math.round(moveX),
//                            ActivityManageUtils.getMyApplication().headTop + 20,
//                            Math.round(getWidth() + moveX),
//                            Math.round(height+ActivityManageUtils.getMyApplication().headTop +20));
//                break;
//        }
//
//        return  true;
//    }



}
