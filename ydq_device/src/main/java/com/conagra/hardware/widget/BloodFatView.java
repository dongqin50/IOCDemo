package com.conagra.hardware.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.conagra.R;
import com.conagra.mvp.utils.DensityUtil;

import static com.conagra.R.color.bloodsugarview2_color1;
import static com.conagra.R.color.bloodsugarview2_color2;


/**
 * Created by yedongqin on 2016/10/27.
 */

public class BloodFatView extends View{

    private int width;
    private int height;
    private PointF centerF;
    private int radius;
    private int inRadius;
    private float startPosition;
    private Context context;
    private double rate;
    /**分段颜色*/

    public BloodFatView(Context context) {
        super(context);
        this.context = context;
    }

    public BloodFatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public BloodFatView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        radius = (Math.min(width,height) - 40)/2;
        startPosition = 120.0f;
        rate = Math.PI/180;
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
        paint.setColor(getResources().getColor(bloodsugarview2_color1));
        RectF outF = new RectF(centerF.x-radius,centerF.y - radius, centerF.x + radius, centerF.y + radius);
        paint.setStrokeWidth(DensityUtil.dp2px(context,9));
        canvas.drawArc(outF,0,45,false,paint);
        canvas.drawArc(outF,135,225,false,paint);
        paint.setStrokeWidth(DensityUtil.dp2px(context,1));
        inRadius = radius  - DensityUtil.dp2px(context,4);
        RectF inF = new RectF(centerF.x-inRadius,centerF.y - inRadius, centerF.x + inRadius, centerF.y + inRadius);
        paint.setColor(getResources().getColor(R.color.register_text1));
        canvas.drawArc(inF,0,45,false,paint);
        canvas.drawArc(inF,135,225,false,paint);

        inRadius = inRadius  - DensityUtil.dp2px(context,30);
        inF = new RectF(centerF.x-inRadius,centerF.y - inRadius, centerF.x + inRadius, centerF.y + inRadius);
        paint.setColor(getResources().getColor(bloodsugarview2_color1));
        paint.setStrokeWidth(DensityUtil.dp2px(context,2));
        RectF rectF = getAntiAliasPoint(rate*30,inRadius,DensityUtil.dp2px(context,30));
        canvas.drawLine(rectF.left,rectF.top,rectF.right,rectF.bottom,paint);
        rectF = getAntiAliasPoint(rate*150,inRadius,DensityUtil.dp2px(context,30));
        canvas.drawLine(rectF.left,rectF.top,rectF.right,rectF.bottom,paint);
        rectF = getAntiAliasPoint(rate*210,inRadius,DensityUtil.dp2px(context,30));
        canvas.drawLine(rectF.left,rectF.top,rectF.right,rectF.bottom,paint);
        rectF = getAntiAliasPoint(rate*330,inRadius,DensityUtil.dp2px(context,30));
        canvas.drawLine(rectF.left,rectF.top,rectF.right,rectF.bottom,paint);
        paint.setStyle(Paint.Style.FILL);
        inRadius = DensityUtil.dp2px(context,30);
        paint.setStrokeWidth(DensityUtil.dp2px(context,1));
        paint.setColor(getResources().getColor(bloodsugarview2_color1));
        inF = new RectF(centerF.x - inRadius/2,centerF.y-inRadius/2,centerF.x+inRadius/2,centerF.y + inRadius/2);
        canvas.drawArc(inF,0,360,true,paint);
        paint.setColor(getResources().getColor(bloodsugarview2_color2));
        canvas.drawCircle(centerF.x,centerF.y,DensityUtil.dp2px(context,10),paint);
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
