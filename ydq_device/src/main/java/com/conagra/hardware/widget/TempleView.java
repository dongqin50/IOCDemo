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

public class TempleView extends View {
    private int width;
    private int height;
    private PointF centerF;
    private int radius;
    private int inRadius;
    private float startPosition;
    private Context context;
    private float initArc;
    private float rate;
    private float currentArc;
    private float currentTemple;
    private float initTemple;


    /**分段颜色*/
    private  int[] SECTION_COLORS = {
            getResources().getColor(R.color.temple1_color6),
            getResources().getColor(R.color.temple1_color6),
            getResources().getColor(R.color.temple1_color6),
            getResources().getColor(R.color.temple1_color1),
            getResources().getColor(R.color.temple1_color2),
            getResources().getColor(R.color.temple1_color3),
            getResources().getColor(R.color.temple1_color4),
            getResources().getColor(R.color.temple1_color4),
            getResources().getColor(R.color.temple1_color5),
            getResources().getColor(R.color.temple1_color6)};

    public TempleView(Context context) {
        super(context);
        this.context = context;
    }

    public TempleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public TempleView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        rate = 280/48f;
        initArc = 10 + 4*rate;
        initTemple = 34.6f;
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
        paint.setStrokeCap(Paint.Cap.ROUND);
        RectF outF = new RectF(centerF.x-radius,centerF.y - radius, centerF.x + radius, centerF.y + radius);
        paint.setStrokeWidth(DensityUtil.dp2px(context,8));
//        LinearGradient lg=new LinearGradient(centerF.x-radius/2,centerF.y-radius/2,centerF.x + radius,centerF.y +radius, SECTION_COLORS,null, Shader.TileMode.MIRROR);
        //参数一为渐变起初点坐标x位置，参数二为y轴位置，参数三和四分辨对应渐变终点，最后参数为平铺方式，这里设置为镜像

        SweepGradient sg = new SweepGradient(centerF.x,centerF.y,SECTION_COLORS,null);
//        paint.setShader(lg);
        paint.setShader(sg);
        canvas.drawArc(outF,120,300,false,paint);
        paint.setStrokeWidth(DensityUtil.dp2px(context,2));
        inRadius = radius  +  DensityUtil.dp2px(context,14);
        RectF inF = new RectF(centerF.x-inRadius,centerF.y - inRadius, centerF.x + inRadius, centerF.y + inRadius);
        paint.setShader(null);
        paint.setColor(getResources().getColor(R.color.line_color2));
        canvas.drawArc(inF,120,300,false,paint);
        paint.setColor(getResources().getColor(R.color.colorMain));
        double angleRate =  Math.PI/180;
        float arc = 0;
        int value  = 4 ;
        if(currentTemple != 0){
            value  = (int) ((currentTemple - initTemple + 0.1) * 10);
        }
        arc = rate* value + 10;
        if(arc <= 0){
            arc = 1;
        }else if(arc > 300){
            arc = 300;
        }
        canvas.drawArc(inF,120,arc,false,paint);

        paint.setStyle(Paint.Style.FILL);
        float circleX = Float.valueOf(Double.valueOf(inRadius * Math.cos(angleRate*(arc + 120))).toString()) + centerF.x;
        float circleY =  Float.valueOf(Double.valueOf(inRadius * Math.sin(angleRate*(arc+ 120))).toString())   + centerF.y;
        canvas.drawCircle(circleX,circleY,DensityUtil.dp2px(context,3),paint);

//        canvas.drawArc(outF,120,startArc,false,paint);
        paint.setStrokeWidth(DensityUtil.dp2px(context,1));

        for(int i = 0; i < 49; i++){
            paint.setColor(getResources().getColor(R.color.white));
            RectF rectF = getAntiAliasPoint(10 + (rate * i + 120),radius - DensityUtil.dp2px(context,8) ,radius + DensityUtil.dp2px(context,8));
            canvas.drawLine(rectF.left,rectF.top,rectF.right,rectF.bottom,paint);
            if(i == 3){
                paint.setColor(getResources().getColor(R.color.colorMain));
            }
//            canvas.drawArc(outF,10 + i * rate ,10 + i * rate  + 1,false,paint);
        }
    }


    public void setCurrentTemple(float currentTemple) {
        this.currentTemple = currentTemple;
        postInvalidate();
    }

    /**
     * 获取刻度线
     * @param angle
     * @return
     */
    private RectF getAntiAliasPoint(double angle,int startRadius,int endRadius){
        angle = angle * Math.PI/180;
        float startX = Float.valueOf(Double.valueOf(startRadius * Math.cos(angle)).toString()) + centerF.x;
        float startY =  Float.valueOf(Double.valueOf(startRadius * Math.sin(angle)).toString())   + centerF.y;

        float endX = Float.valueOf(Double.valueOf(endRadius * Math.cos(angle)).toString())   + centerF.x;
        float endY = Float.valueOf(Double.valueOf(endRadius * Math.sin(angle)).toString())   + centerF.y;

        return new RectF(startX,startY,endX,endY);
    }

//    private double


}
