package com.conagra.hardware.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.conagra.R;
import com.conagra.mvp.model.ValuesBean;
import com.conagra.mvp.model.ValuesListBean;
import com.conagra.mvp.utils.LogMessage;
import com.conagra.mvp.utils.TimeUtils;

/**
 * Created by yedongqin on 16/10/6.
 */
public class QuickeningView1 extends View {

    private int width;
    private int height;
    private float radiusOut;
    private float radius;
    private float radiusIn;

    private float radiusTime;
    private float radiusIcon;

    private PointF centerPoint;
    private String mTime = "60:00";
    private long currentTime;
    private double rate;    //比率
    private ValuesListBean<Double,Long> timeIntervalList;       //间隔时间，对应的时间值
    private Bitmap bitmap;
    private double initAngle;
    private int currentIndex = -1;


    public QuickeningView1(Context context) {
        super(context);
        timeIntervalList = new ValuesListBean<>();
        initIcon();
    }

    public QuickeningView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        timeIntervalList = new ValuesListBean<>();
        initIcon();
    }

    public QuickeningView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        timeIntervalList = new ValuesListBean<>();
        initIcon();
    }

    private void initIcon(){
        initAngle = 19.0/36.0 * Math.PI ;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.quickening_foot_point);
        Matrix matrix = new Matrix();
        matrix.setScale(0.7f,0.7f);
        bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
        postInvalidate();
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
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
        radiusOut = (height-150)/2;
//        radiusTime = radiusOut -  Math.max(bitmap.getWidth(),bitmap.getHeight());
        radiusTime = radiusOut - 20;
//        radiusIcon = radiusOut - Math.max(bitmap.getWidth(),bitmap.getHeight())/2;
        radiusIcon = radiusOut ;

        radiusIn = radiusOut/2;
        radius = radiusIn*8/9;
        centerPoint = new PointF(width/2,height/2); //中点坐标
        rate = (2.0 * Math.PI * 3.0) /(60.0 * 60.0 * 1000.0);  //比率
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        drawTime(canvas);
          drawCircleOut(canvas);
          drawCircleIn(canvas);
//        drawTdIcon(canvas);
    }

    /**
     * 点击产生胎动
     */
    public void doClick(long time){

        if(currentTime == 0){
            currentTime = time;
        }

        long intevalTime = time - currentTime;
        ValuesBean<Double,Long> valuesBean = new ValuesBean<>();
        valuesBean.setValue2List(time);
        double angle = (initAngle + intevalTime) * rate;
        valuesBean.setValue1List(angle);
//        valuesBean.setValue2(time);
//        Double.valueOf(radiusTime * Math.cos(((Double) vb.getValue1(0) +  )) + centerPoint.x
        LogMessage.doLogErrorMessage("Message1","angle : " + ((angle * 180)/Math.PI)+ " intevalTime:" + intevalTime);
        timeIntervalList.add(valuesBean);
//        currentTime = time;
        postInvalidate();
    }

    public void doClear(){
        timeIntervalList.clear();
        currentTime = 0;
        currentIndex = -1;
        postInvalidate();
    }

    //画最外面的圆
    private void drawCircleOut(Canvas canvas){

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.mine_bg));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(16);

        RectF outF = new RectF(centerPoint.x-radiusOut,centerPoint.y - radiusOut, centerPoint.x + radiusOut, centerPoint.y + radiusOut);
        RectF inF = new RectF(centerPoint.x-radiusIn,centerPoint.y - radiusIn, centerPoint.x + radiusIn, centerPoint.y + radiusIn);
//
        canvas.drawArc(outF,90,85,false,paint);
        canvas.drawArc(outF,180,85,false,paint);
        canvas.drawArc(outF,270,85,false,paint);
        canvas.drawArc(outF,0,85,false,paint);

        PointF startP = new PointF(centerPoint.x,centerPoint.y + radiusOut);

        paint.setStrokeWidth(2);
        paint.setTextSize(14);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.black));
        int point =  (Math.max(bitmap.getWidth(),bitmap.getHeight())/2);

        for(ValuesBean vb : timeIntervalList){

            PointF endP2 = new PointF();
            endP2.x = Float.parseFloat(Double.valueOf(
                    radiusIcon * Math.cos((Double) vb.getValue1(0)) + centerPoint.x - point).toString());
            endP2.y = Float.parseFloat(Double.valueOf(
                    radiusIcon * Math.sin((Double)vb.getValue1(0)) + centerPoint.y - point).toString());
//            drawTdIcon(canvas,endP2.x,endP2.y);
//            int radius = Math.max(bitmap.getWidth(),bitmap.getHeight());
            canvas.drawBitmap(bitmap,endP2.x,endP2.y,paint);
            String text = TimeUtils.getCurrentTimeByHM((Long) vb.getValue2(0));
            float length = paint.measureText(text);
//            radiusTime = radiusTime - length/2;
            PointF endP1 = new PointF();
            endP1.x = Float.parseFloat(Double.valueOf((radiusTime - length/2) * Math.cos((Double)vb.getValue1(0)) + centerPoint.x - length/2).toString());
            endP1.y = Float.parseFloat(Double.valueOf((radiusTime - length/2) * Math.sin((Double)vb.getValue1(0)) + centerPoint.y).toString());
            canvas.drawText(text,endP1.x,endP1.y,paint);


        }
    }

    /**
     * 里面的圆，每次胎动标记,并纪录时间，
     * @param canvas
     */
    private void drawCircleIn(Canvas canvas){

        int count = 36;
        double angleMargin = 2 * Math.PI/count;
        Paint paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.index_yy));

        for(int i = 0; i < count; i++){

            double angle = angleMargin * i;
            RectF rectF = getAntiAliasPoint(angle);

            if(i == count/3){
                paint.setColor(getResources().getColor(R.color.colorPick));
            }else if(i == count * 2/3){
                paint.setColor(getResources().getColor(R.color.colorMain));
            }else {
                paint.setColor(getResources().getColor(R.color.index_yy));
            }

//            long l = timeIntervalList.getValues2(timeIntervalList.size() - 1).get(0) - currentTime;
//            int ii = Math.round(l/(1000*60 * 60));
            if(currentIndex >= 0){

                if( i < currentIndex){
                    LogMessage.doLogMessage("Message1",currentIndex + "     message");
                    paint.setColor(getResources().getColor(R.color.color_text1));
                }

            }

            canvas.drawLine(rectF.left,rectF.top,rectF.right,rectF.bottom,paint);

        }
    }



//    /**
//     * 胎动图标
//     */
//    public void drawTdIcon(Canvas canvas,float left,float top){
//
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setColor(getResources().getColor(R.color.index_yy));
//
//
//    }



    /**
     * 获取刻度线
     * @param angle
     * @return
     */
    private RectF getAntiAliasPoint(double angle){

        float startX = Float.valueOf(Double.valueOf(radiusIn * Math.cos(angle)).toString()) + centerPoint.x;
        float startY =  Float.valueOf(Double.valueOf(radiusIn * Math.sin(angle)).toString())   + centerPoint.y;

        float endX = Float.valueOf(Double.valueOf(radius * Math.cos(angle)).toString())   + centerPoint.x;
        float endY = Float.valueOf(Double.valueOf(radius * Math.sin(angle)).toString())   + centerPoint.y;

        return new RectF(startX,startY,endX,endY);
    }


}
