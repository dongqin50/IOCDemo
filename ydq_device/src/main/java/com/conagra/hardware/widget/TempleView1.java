package com.conagra.hardware.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.conagra.R;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.model.TempleListModel;
import com.conagra.mvp.model.ValuesBean;
import com.conagra.mvp.utils.LogMessage;
import com.conagra.mvp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yedongqin on 2016/10/27.
 */

public class TempleView1 extends View {
    /**
     * view宽,高
     */
    private float width;
    private float height;
    /**
     * 行间距,列间距
     */
    private Map<String,ValuesBean<String,Double>> mDataMap;
    private List<PointF> mCountPointFList;
    private float marginY;
    private float marginX;
    private float lengthX;
    private float lenghtY;
    private int startX;
    private int startY;
    private int startTimePosition;
    private String currentTime;
    private BaseListener.Observable1<String,String> observable1;
    public void setObservable1(BaseListener.Observable1<String,String> observable1) {
        this.observable1 = observable1;
    }

    public TempleView1(Context context) {
        this(context,null);
    }

    public TempleView1(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TempleView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        doInit(context);
    }

    public void doInit(Context context) {
        mDataMap = new HashMap<>();
        mCountPointFList = new ArrayList<>();

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

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if(layoutParams != null && layoutParams instanceof ViewGroup.MarginLayoutParams){
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            width = width - marginLayoutParams.leftMargin - marginLayoutParams.rightMargin;
            height = height - marginLayoutParams.topMargin - marginLayoutParams.bottomMargin;
        }
        setMeasuredDimension((int)width,(int)height);
        startX = 20;
        startY = 10;
        lengthX = 24; //横坐标
        lenghtY = 15; //
        marginX = (width - startX)/lengthX;
        marginY = (height - startY)/lenghtY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawForm(canvas);
    }

    private void drawForm(Canvas canvas){
        mCountPointFList.clear();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
//        paint.setColor(getResources().getColor(R.color.quickening_text1));
//        paint.setTextSize(24);
//        float length = paint.measureText("血压");
//        canvas.drawText("体温标准",20,60,paint);
        paint.setColor(getResources().getColor(R.color.quickening_text1));
//        paint.setTextSize(20);
//        canvas.drawText("mmol",20 + length-2,80,paint);
        paint.setStrokeWidth(1);
        paint.setTextSize(22);
        paint.setColor(getResources().getColor(R.color.colorMain));

        //画竖线
        for(int x = 0; x < lengthX;x++){
            float x1 = marginX * x + startX;
//            int num = (int) (((width/margin) - 2) / 3);
            paint.setColor(getResources().getColor(R.color.quickening_text1));
            int nu1 = (int) (x- (width/marginX - 2))/3;
//            String time1 = TimeUtils.getDate(nu1);
//            PointF sF = new PointF();
//            sF.x = x1;
            //横坐标
            if(x % 3 == 2 ){
                if (x < lengthX - 2){
                    PointF cF = new PointF();
//                    cF.x = x1;
                    int nu = (int) (x- (width/marginX - 3))/3;
                    String time = TimeUtils.getDate(nu - startTimePosition);
                    float lg = paint.measureText(time);

                    //横坐标
                    canvas.drawText(time,x1 - lg/2 , lenghtY * marginY + startY - marginY/2,paint);

                    //12小时
                    if(mDataMap.containsKey(time)){
                        ValuesBean<String,Double> value = mDataMap.get(time);

                        int sec =  TimeUtils.intervalTime("2016-" + time + " 00:00:00",value.getValue1(0));
                        float secf = sec/24/3600f;
                        float xx = secf* 3 * marginX + x1;
                        cF.x = xx;
                        if(value != null){

                            Float fl  = Float.parseFloat(String.valueOf(value.getValue2(0)));
                            if(fl < 35.0){
                                fl = 35.0f;
                            }else if(fl > 42.0){
                                fl = 42.0f;
                            }

//                            fl = 38.7f;
                            cF.y = 84 * marginY + startY - 2 * (fl*marginY) ;
//                        cF.y = Float.parseFloat(value);
                            mCountPointFList.add(cF);
                        }
                    }
                }

                paint.setStrokeWidth(2);
            }


            paint.setColor(getResources().getColor(R.color.colorMain));

            canvas.drawLine(x1,startY,x1,(lenghtY -1)*marginY + startY,paint);
            paint.setStrokeWidth(1);
        }

        //画横线 和左右坐标
        for(int y = 0; y < lenghtY; y++){
//            paint.setColor(getResources().getColor(R.color.colorMain));
            //画横线
            float y1 = marginY * y + startY;

            paint.setColor(getResources().getColor(R.color.quickening_text1));
            if(y % 2 == 0 && y < lenghtY){

                String tx1 = (42.0 -  y/2) + "";
                float lg = paint.measureText(tx1);

                //左边坐标
                canvas.drawText(tx1,marginX + startX-lg - 10,y1 + 5,paint);
                paint.setStrokeWidth(2);
            }
            paint.setColor(getResources().getColor(R.color.colorMain));
            canvas.drawLine(0,y1,width,y1,paint);
            paint.setStrokeWidth(1);

        }
        //总次数标记,并画折线
        paint.setColor(getResources().getColor(R.color.colorPick));
        for(int i = 0; i < mCountPointFList.size(); i++){
            PointF p = mCountPointFList.get(i);
            paint.setStrokeWidth(8);
            canvas.drawCircle(p.x,p.y,6,paint);
            if(i + 1 < mCountPointFList.size()){
                PointF p1 = mCountPointFList.get(i+1);
                paint.setStrokeWidth(2);
                canvas.drawLine(p.x,p.y,p1.x,p1.y,paint);
            }
        }
    }

    public void updataData(List<TempleListModel> model , String currentTime){
//        FetalMovementListBakeModel dataWeekBeanList = fetalMovementList.get(position);
        mDataMap.clear();
        mCountPointFList.clear();
        if(model != null && !TextUtils.isEmpty(currentTime)){
            startTimePosition = TimeUtils.intervalDay(TimeUtils.getYMDData(0),currentTime);
            this.currentTime = currentTime;
            if(model != null ) {
                for (TempleListModel dataWeekBean : model) {
                    if (dataWeekBean != null) {
                        LogMessage.doLogMessage("BaseActivity"," fragment : " + dataWeekBean.getTemperatureValue());
                        String time = dataWeekBean.getCreateTime();
                        int day = TimeUtils.intervalDay(TimeUtils.getYMDData(0),time);
                        time = TimeUtils.getDate(-day);
                        ValuesBean<String,Double> bean = new ValuesBean<>();
                        bean.setValue1List(dataWeekBean.getCreateTime());
                        bean.setValue2List(dataWeekBean.getTemperatureValue());
                        mDataMap.put(time,bean);
                    }
                }
            }
        }

        /**
         * 重新绘制
         */
        postInvalidate();
    }
    private float currentX;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        int action = event.getAction();
        switch (action){

            case  MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                scrllerTime = System.currentTimeMillis();
                break;
            case  MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                break;
            case  MotionEvent.ACTION_UP:
                long time =  System.currentTimeMillis()- scrllerTime;
                float x = currentX - lastX;
                if(x > 0){
                    if(observable1 != null){
                        observable1.onAction1(currentTime);
                    }
                }else if(x < 0){
                    if(observable1 != null){
                        observable1.onAction(currentTime);
                    }
                }
                break;
        }

//        return true;
        return true;
    }

    private float lastX;
    private long scrllerTime;

}
