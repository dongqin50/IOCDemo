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
 * Created by yedongqin on 2016/10/25.
 */

public class BloodPressureView1 extends View {

    private int width;
    private int height;
    private PointF centerF;
    private int radius;
    private int inRadius;
    private float startPosition;
    private Context context;

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
    public BloodPressureView1(Context context) {
        super(context);
        this.context = context;
    }

    public BloodPressureView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public BloodPressureView1(Context context, AttributeSet attrs, int defStyleAttr) {
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
        paint.setStrokeWidth(DensityUtil.dp2px(context,8));
//        LinearGradient lg=new LinearGradient(centerF.x-radius/2,centerF.y-radius/2,centerF.x + radius,centerF.y +radius, SECTION_COLORS,null, Shader.TileMode.MIRROR);
        //参数一为渐变起初点坐标x位置，参数二为y轴位置，参数三和四分辨对应渐变终点，最后参数为平铺方式，这里设置为镜像

        SweepGradient sg = new SweepGradient(centerF.x,centerF.y,SECTION_COLORS,null);
//        paint.setShader(lg);
        paint.setShader(sg);
        canvas.drawArc(outF,120,300,false,paint);
        paint.setStrokeWidth(DensityUtil.dp2px(context,1));
        inRadius = radius  - DensityUtil.dp2px(context,4);
        RectF inF = new RectF(centerF.x-inRadius,centerF.y - inRadius, centerF.x + inRadius, centerF.y + inRadius);
        paint.setShader(null);
        paint.setColor(getResources().getColor(R.color.line_color2));
        canvas.drawArc(inF,120,300,false,paint);

    }


}
