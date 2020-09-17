package com.conagra.hardware.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.conagra.R;
import com.conagra.mvp.utils.DensityUtil;
import com.conagra.mvp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yedongqin on 16/10/7.
 */
public class LineTimePointView extends View {

    private float width;
    private float height;
    private float rowMargin;
    private String startTime;
//    private long currentTime;
//    private StringBuilder mBuilder;
//    private float rate;
    private List<String> mList;
    private Context context;

    public LineTimePointView(Context context) {
        this(context,null,0);
    }

    public LineTimePointView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LineTimePointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        mBuilder = new StringBuilder();
        mList = new ArrayList<>();
        this.context = context;

//        start();
//        addValue();
    }

    public void start(String startTime){
        if(TextUtils.isEmpty(startTime))
            return;

        this.startTime= startTime;
    }

    public void doClear(){
//        mBuilder.delete(0,mBuilder.length());
//        mBuilder.setLength(0);
//        postInvalidate();
    }

//    public void addValue(String  currentTime){
//
//        mBuilder.append(currentTime + ";");
//        postInvalidate();
//    }

    public void updateValue(String startTime,List<String> mList){

        if(mList == null || TextUtils.isEmpty(startTime)){
            this.mList.clear();
            postInvalidate();
            return;
        }
//        doClear();
        this.startTime = startTime;
        this.mList = mList;


        postInvalidate();
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

        setMeasuredDimension((int)width,(int)height);
        rowMargin = (width-10) / (60 * 60);
//        rate = width/(60 * 60);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);  //画线
        drawPoint(canvas);
    }

    private void drawPoint(Canvas canvas){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
//        paint.setColor(getResources().getColor(R.color.colorMain));
//        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DensityUtil.dp2px(context,6));
        paint.setTextSize(DensityUtil.dp2px(context,10));
        if(mList != null && mList.size() > 0 && !TextUtils.isEmpty(startTime)){
//            String[] points = mBuilder.toString().substring(0,mBuilder.toString().length() - 1).split(";");
            for(int i = 0; i < mList.size();i++){
                String p = mList.get(i);
//                Long value = Long.valueOf(p);
                p = startTime.split("T")[0] + " " + p + ":00";
                float x = Float.valueOf(Integer.valueOf(TimeUtils.intervalTime(p,startTime)).toString()) * rowMargin;
//                String time = TimeUtils.getCurrentTimeByHM(p);
//                String time = TimeUtils.getCurrentTimeByHM(TimeUtils.getCurrentTimeStringByHM(p));

                paint.setColor(getResources().getColor(R.color.black));
                if(i % 2 == 0){
                    canvas.drawText(mList.get(i),x + 20 ,(height/2)-20 ,paint);
                }else{
                    canvas.drawText(mList.get(i),x + 20 ,(height/2) + 30 ,paint);
                }
                paint.setColor(getResources().getColor(R.color.colorMain));
                canvas.drawCircle(x +  48,height/2,4,paint);
            }
        }
    }

    private void drawLine(Canvas canvas){

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorDrak));
//        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DensityUtil.dp2px(context,2));

        canvas.drawLine(40,height/2,width - 40,height/2,paint);
    }
}
