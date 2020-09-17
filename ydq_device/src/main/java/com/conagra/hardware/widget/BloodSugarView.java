package com.conagra.hardware.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.conagra.R;
import com.conagra.mvp.utils.DensityUtil;


/**
 * Created by yedongqin on 2016/10/27.
 */

public class BloodSugarView extends View {

    private int width;
    private int height;
    private PointF centerF;
    private int radius;
    private int inRadius;
    private float startPosition;
    private Context context;
    private double rate;
    private double mCurrentValue;
    private float maxValue;
    private float startArc;
    private float endArc;

    private static final String TAG = "BloodSugarView";


    /**分段颜色*/
    private  int[] SECTION_COLORS = {
            getResources().getColor(R.color.line_color3),
            getResources().getColor(R.color.line_color3),
            getResources().getColor(R.color.line_color4),
            getResources().getColor(R.color.line_color5),
            getResources().getColor(R.color.line_color6),
            getResources().getColor(R.color.line_color7),
            getResources().getColor(R.color.line_color3),
            getResources().getColor(R.color.line_color3)};

    public BloodSugarView(Context context) {
        super(context);
        this.context = context;
    }

    public BloodSugarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public BloodSugarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
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
        centerF = new PointF(width/2,height/2);
        radius = (Math.min(width,height) - 100)/2;
        startPosition = 120.0f;
        rate = Math.PI/180;
        maxValue = 20.0f;
        startArc = 130.0f;
        endArc = 280.0f;

    }

    public void setmCurrentValue(double mCurrentValue) {
        this.mCurrentValue = mCurrentValue;
        postInvalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
    }

    /**
     * 画圆
     * @param canvas
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawCircle(Canvas canvas){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        RectF outF = new RectF(centerF.x-radius,centerF.y - radius, centerF.x + radius, centerF.y + radius);

        //最内部的圆
        paint.setStrokeWidth(DensityUtil.dp2px(context,7));
        SweepGradient sg = new SweepGradient(centerF.x,centerF.y,SECTION_COLORS,null);
        paint.setShader(sg);
        canvas.drawArc(outF,startArc,endArc,false,paint);
        //最内部的圆
        paint.setStrokeWidth(DensityUtil.dp2px(context,1));
        paint.setShader(null);
        paint.setColor(getResources().getColor(R.color.bloodsugarview_color4));
        for(int i = 0; i < maxValue*2; i++){
            RectF rectF = getAntiAliasPoint((i * (endArc/(maxValue*2)) + startArc)*rate,radius + DensityUtil.dp2px(context,7),radius - DensityUtil.dp2px(context,2));
            canvas.drawLine(rectF.left,rectF.top,rectF.right,rectF.bottom,paint);
        }

        paint.setDither(true);
        inRadius = radius  +  DensityUtil.dp2px(context,7);
        RectF inF = new RectF(centerF.x-inRadius,centerF.y - inRadius, centerF.x + inRadius, centerF.y + inRadius);
        paint.setColor(getResources().getColor(R.color.bloodsugarview_color2));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(DensityUtil.dp2px(context,12));
        canvas.drawArc(inF,startArc,endArc,false,paint);
        paint.setStrokeWidth(DensityUtil.dp2px(context,10));
        paint.setColor(getResources().getColor(R.color.bloodsugarview_color1));
        canvas.drawArc(inF,startArc,endArc,false,paint);
        paint.setColor(getResources().getColor(R.color.colorMain));
        if(mCurrentValue >= maxValue){
            mCurrentValue = maxValue;
        }else if(mCurrentValue <= 0){
            mCurrentValue = 0.01;
        }

//        LogMessage.doLogMessage(TAG," BloodSugarLayout1 :  " + mCurrentValue);
        float angle = (float) (56 * mCurrentValue /3) * 15/maxValue ;
        canvas.drawArc(inF,startArc,angle,false,paint);

//        canvas.drawOval();
    }

    /**
     * 获取刻度线
     * @param angle
     * @return
     */
    private RectF getAntiAliasPoint(double angle,int startRadius,int endRadius){

        float startX = Float.valueOf(Double.valueOf(startRadius * Math.cos(angle)).toString()) + centerF.x;
        float startY =  Float.valueOf(Double.valueOf(startRadius * Math.sin(angle)).toString())   + centerF.y;

        float endX = Float.valueOf(Double.valueOf(endRadius * Math.cos(angle)).toString())   + centerF.x;
        float endY = Float.valueOf(Double.valueOf(endRadius * Math.sin(angle)).toString())   + centerF.y;

        return new RectF(startX,startY,endX,endY);
    }

}
