package com.conagra.hardware.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.conagra.R;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.constant.TypeApi;
import com.conagra.mvp.model.BloodSugarListRowModel;
import com.conagra.mvp.utils.LogMessage;
import com.conagra.mvp.utils.TimeUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by yedongqin on 2016/10/27.
 */

public class BloodSugarView1 extends View {

    /**
     * view宽,高
     */
    private float width;
    private float height;
    /**
     * 行间距,列间距
     */
    private Map<Integer,String> mCountTdMap;
//    private List<PointF> pointFList;
    private float marginY;
    private float marginX;
    private float lengthX;
    private float lenghtY;
    private int startX;
    private int startY;
    private String currentTime;
    private BaseListener.Observable1<String,String> observable1;
    private List<String> xList = Arrays.asList("6:00","9:00","12:00","15:00","18:00","21:00","24:00") ;
    public void setObservable1(BaseListener.Observable1<String,String> observable1) {
        this.observable1 = observable1;
    }

    public BloodSugarView1(Context context) {
        this(context,null);
    }

    public BloodSugarView1(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BloodSugarView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        doInit(context);
    }

    public void doInit(Context context) {
//        pointFList = new ArrayList<>();
        mCountTdMap = new TreeMap<>((o1,o2)->{

            if(o1 > o2){
                return 1;
            }else if(o1 == o2){
                return 0;
            }else {
                return -1;
            }

//            if(!TextUtils.isEmpty(o1) && !TextUtils.isEmpty(o2) && CommonUtils.isNumber(o1)&& CommonUtils.isNumber(o2)){
//
//                int v1 = Integer.parseInt(o1);
//                int v2 = Integer.parseInt(o2);
//                if(v1 > v2){
//                    return 1;
//                }else if(v1 == v2){
//                    return 0;
//                }else {
//                    return -1;
//                }
//            }
//            return -1;
        });
        startX = 20;
        startY = 100;
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
        lengthX = 21; //横坐标
        lenghtY = 13; //
        marginX = (width - startX)/lengthX;
        marginY = (height - startY)/lenghtY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawForm(canvas);
    }


    private void drawForm(Canvas canvas){

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.quickening_text1));
        paint.setTextSize(28);
        paint.setStyle(Paint.Style.STROKE);
        float length = paint.measureText("血糖曲线图");
        canvas.drawText("血糖曲线图",20,60,paint);
        paint.setColor(getResources().getColor(R.color.quickening_text1));
//        paint.setTextSize(20);
//        canvas.drawText("mmol",20 + length-2,80,paint);
        paint.setStrokeWidth(1);
        paint.setTextSize(22);
        paint.setColor(getResources().getColor(R.color.colorMain));

        //画竖线
        for(int x = 1; x < lengthX;x++){
            float x1 = marginX * x + startX;
            paint.setColor(getResources().getColor(R.color.quickening_text1));
//
            //横坐标
            if(x % 3 == 2 && x < lengthX ){
                PointF cF = new PointF();
                cF.x = x1;
                int nu =  (x-2)/3;
                String time = xList.get(nu);
                float lg = paint.measureText(time);

                //横坐标
                canvas.drawText(time,x1 - lg/2 , lenghtY * marginY + startY ,paint);
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
            if(y % 2 == 0){

                String tx1 = (12 -  y) + "";
                float lg = paint.measureText(tx1);
                if(y == 0){
                    tx1 += "+";
                }

                //左边坐标
                canvas.drawText(tx1,marginX + startX-lg - 10,y1 + 5,paint);
                paint.setStrokeWidth(2);
            }
            paint.setColor(getResources().getColor(R.color.colorMain));
            canvas.drawLine(0,y1,width,y1,paint);
            paint.setStrokeWidth(1);

        }
//        //画单次标记,并画折线
//
//        paint.setColor(getResources().getColor(R.color.quickening_line_o));
//        for(int i = 0; i < mSinglePointFList.size(); i++){
//            PointF p = mSinglePointFList.get(i);
//            paint.setStrokeWidth(8);
//            canvas.drawCircle(p.x,p.y,6,paint);
//            if(i + 1 < mSinglePointFList.size()){
//                PointF p1 = mSinglePointFList.get(i+1);
//                paint.setStrokeWidth(2);
//                canvas.drawLine(p.x,p.y,p1.x,p1.y,paint);
//            }
//        }
//        //总次数标记,并画折线
//        paint.setColor(getResources().getColor(R.color.colorPick));
//        for(int i = 0; i < mCountPointFList.size(); i++){
//            PointF p = mCountPointFList.get(i);
//            paint.setStrokeWidth(8);
//            canvas.drawCircle(p.x,p.y,6,paint);
//            if(i + 1 < mCountPointFList.size()){
//                PointF p1 = mCountPointFList.get(i+1);
//                paint.setStrokeWidth(2);
//                canvas.drawLine(p.x,p.y,p1.x,p1.y,paint);
//            }
//        }

        int i = 0;
        Path path = new Path();
        Set<Map.Entry<Integer,String>> set= mCountTdMap.entrySet();
        paint.setColor(getResources().getColor(R.color.quickening_line_o));
        paint.setStrokeWidth(3);
        for(Map.Entry<Integer,String> entry : set){
            PointF pointF = new PointF();
            Integer key = entry.getKey();
            String[] value = entry.getValue().split(";");

            Float fl = Float.parseFloat(value[1]);
            if(fl > 12){
                fl = Float.valueOf(12);
            }else if(fl < 0){
                fl = Float.valueOf(0);
            }
            String[] vs = TimeUtils.getTime(value[0],"yyyy-MM-dd HH:mm:ss","HH:mm").split(":");
            int hour = Integer.valueOf(vs[0]);
            if(hour < 6 ){
                value[0] = "06" + vs[1];
            }

            pointF.y = 12 * marginY + startY - (fl * marginY) ;
            pointF.x = marginX  * (TimeUtils.intervalTime(value[0], currentTime + " 06:00:00"))/3600 + startX + 2 * marginX;

            if(i == 0){
                path.moveTo(pointF.x ,pointF.y);
                i++;
            }else {
                path.lineTo(pointF.x,pointF.y);
            }

//            paint.setStrokeWidth(8);
//            showData(Integer.parseInt(key),paint);
//            canvas.drawCircle(pointF.x,pointF.y,6,paint);
//            paint.setColor(getResources().getColor(R.color.quickening_line_o));
        }
        canvas.drawPath(path,paint);
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.FILL);
        for(Map.Entry<Integer,String> entry : set){
            PointF pointF = new PointF();
            Integer key = entry.getKey();
            String[] value = entry.getValue().split(";");

            Float fl = Float.parseFloat(value[1]);
            pointF.y = 12 * marginY + startY - (fl * marginY) ;
            pointF.x = marginX * (TimeUtils.intervalTime(
                    value[0], currentTime + " 06:00:00"))/3600 + startX + 2 *  marginX;


            showData(key,paint);
            canvas.drawCircle(pointF.x,pointF.y,6,paint);
        }
    }

    public void updataData(List<BloodSugarListRowModel> dataTimeBeanList, String currentTime){
//        FetalMovementListBakeModel dataWeekBeanList = fetalMovementList.get(position);
        mCountTdMap.clear();
        this.currentTime = currentTime;
        if(dataTimeBeanList == null ){
            return;
        }
        int i = 0;
        for (BloodSugarListRowModel dataTimeBean : dataTimeBeanList) {
            if (dataTimeBean != null) {
                LogMessage.doLogMessage("BaseActivity"," fragment : " + dataTimeBean.getCreateTime());
                mCountTdMap.put(dataTimeBean.getTimeSlot(),dataTimeBean.getCreateTime()+ ";"+(dataTimeBean.getBloodSugarValue()));
            }
            i++;
        }

            /**
             * 重新绘制
             */
//            postInvalidate();
            invalidate();

    }
    private float currentX;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        int action = event.getAction();
        switch (action){

            case  MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                break;
            case  MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                break;
            case  MotionEvent.ACTION_UP:
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


    public void showData(int state,Paint paint){

        switch (state){
            case TypeApi.CATEGORY_AM_BEFORE:        //早餐前
                paint.setColor(getResources().getColor(R.color.bloodsugarview1_color1));
                break;
            case TypeApi.CATEGORY_AM_AFTER:         //早餐后
                paint.setColor(getResources().getColor(R.color.bloodsugarview1_color2));
                break;
            case TypeApi.CATEGORY_PM_BEFORE:        //午餐前
                paint.setColor(getResources().getColor(R.color.bloodsugarview1_color3));
                break;
            case TypeApi.CATEGORY_PM_AFTER:         //午餐后
                paint.setColor(getResources().getColor(R.color.bloodsugarview1_color4));
                break;
            case TypeApi.CATEGORY_NIGHT_BEFORE:         //晚餐前
                paint.setColor(getResources().getColor(R.color.bloodsugarview1_color5));
                break;
            case TypeApi.CATEGORY_NIGHT_AFTER:          //晚餐后
                paint.setColor(getResources().getColor(R.color.bloodsugarview1_color6));
                break;
            case TypeApi.CATEGORY_SLEEP_BEFORE:         //睡觉
                paint.setColor(getResources().getColor(R.color.bloodsugarview1_color7));
                break;
        }
    }


}
