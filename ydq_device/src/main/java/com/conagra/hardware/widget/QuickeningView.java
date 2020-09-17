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
import com.conagra.mvp.model.DateModelBean;
import com.conagra.mvp.model.FetalMoveListsModel;
import com.conagra.mvp.model.FetalMoveListsRowBean;
import com.conagra.mvp.model.RecordTimePointModelBean;
import com.conagra.mvp.model.ValuesBean;
import com.conagra.mvp.utils.LogMessage;
import com.conagra.mvp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yedongqin on 16/10/4.
 *
 * 胎动图
 */
public class QuickeningView extends View{

    public QuickeningView(Context context) {
        super(context);
        doInit(context);
    }

    public QuickeningView(Context context, AttributeSet attrs) {
        super(context, attrs);
        doInit(context);
    }

    public QuickeningView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        doInit(context);
    }

    /**
     * 折线笔
     */
    private Paint mLinePaint;
    private Paint mTextPaint;
    private Paint mPointPaint;

    private float maxTime = 220;

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


    /**
     * 行间距,列间距
     */
//    private float rowMargin;
//    private float colMargin;
//    private StringBuilder mBuilder;
//    private StringBuilder mGSBuilder;
    private Map<String,String> mCountTdMap;
    private Map<String,ValuesBean<Integer,String>> mSingleTdMap;

    private float marginY;
    private float marginX;
    private float lengthX;
    private float lenghtY;
    private int startX;
    private int startY;
    private int startTimePosition;
    private List<PointF> mSinglePointFList;
    private List<PointF> mCountPointFList;
    private String currentTime;
    private BaseListener.Observable1<String,String> observable1;
    //
    private long startTime;

    public void setObservable1(BaseListener.Observable1<String,String> observable1) {
        this.observable1 = observable1;
    }

    public void doInit(Context context) {
        mCountTdMap = new HashMap<>();
        mSingleTdMap = new HashMap<>();
        mSinglePointFList = new ArrayList<>();
        mCountPointFList = new ArrayList<>();
        startX = 20;
        startY = 100;
//        gestureDetector = new GestureDetector(context,onGestureListener);;
//        for(int i = -6; i < 1;i++){
//            String time = TimeUtils.getDate(i);
//            mCountTdMap.put(time,(30 * (-i) + 30) + "");
//            ValuesBean<Integer,String> valuesBean = new ValuesBean<>();
//            valuesBean.setValue1(0,1,2);
//            valuesBean.setValue2((i + 7) + "",(i + 8) + "",(i + 5) + "");
//            if(i % 2 == 0)
//                mSingleTdMap.put(time,valuesBean);
//        }
    }


    /**
     * 添加数值
     * @param singleValue  单次胎动
     */
    public void addSingleValue(String time,int singleTime,String singleValue){

//        if(startTime == 0)
//            return;

//        String time = TimeUtils.getYMDData(0);
        if(mSingleTdMap.containsKey(time)){
            mSingleTdMap.get(time).setValue2(singleTime,singleValue);
        }else {
            ValuesBean<Integer,String> mSingleValues = new ValuesBean<>();
            mSingleValues.setValue1List(0,1,2);
            mSingleValues.setValue2List("","","");
            mSingleValues.setValue2(singleTime,singleValue);
            mSingleTdMap.put(time,mSingleValues);
        }


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
        lengthX = 24; //横坐标
        lenghtY = 15; //
        marginX = (width - startX)/lengthX;
        marginY = (height - startY)/lenghtY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mSinglePointFList.clear();
        mCountPointFList.clear();
        drawForm(canvas);
    }

    private void drawForm(Canvas canvas){

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.quickening_text1));
        paint.setTextSize(28);
        float length = paint.measureText("胎动");
        canvas.drawText("胎动",20,60,paint);
        paint.setColor(getResources().getColor(R.color.quickening_text1));
        paint.setTextSize(20);
        canvas.drawText("次",20 + length-2,80,paint);
        paint.setStrokeWidth(1);
        paint.setTextSize(16);
        paint.setColor(getResources().getColor(R.color.colorMain));

        //画竖线
        for(int x = 1; x < lengthX;x++){
            float x1 = marginX * x + startX;
//            int num = (int) (((width/margin) - 2) / 3);
            paint.setColor(getResources().getColor(R.color.quickening_text1));
            int nu1 = (int) (x- (width/marginX - 2))/3;
            String time1 = TimeUtils.getDate(nu1 - startTimePosition);
            PointF sF = new PointF();
            sF.x = x1;

            if(mSingleTdMap.containsKey(time1) && x > 1 && x < lengthX - 1) {
                ValuesBean<Integer, String> value = mSingleTdMap.get(time1);
                String mm = null;

                switch (x % 3) {
                    case 0:
                        mm = value.getValue2(1);
                        break;
                    case 1:
                        mm = value.getValue2(2);
                        break;
                    case 2:
                        mm = value.getValue2(0);
                        break;
                }

                if (!TextUtils.isEmpty(mm)) {
                    Float fl = Float.parseFloat(mm);
                    if(fl > 0){
                        if(fl > 20){
                            fl = 20f;
                        }
                        sF.y = 12 * marginY + startY - (fl * marginY) / 2;
//                        sF.y = Float.parseFloat(value);
                        mSinglePointFList.add(sF);
                    }
                }
            }

            //横坐标
            if(x % 3 == 2 && x < lengthX - 3){
                PointF cF = new PointF();
                cF.x = x1;
                int nu = (int) (x- (width/marginX - 3))/3;
                String time = TimeUtils.getDate(nu - startTimePosition);
                float lg = paint.measureText(time);

                //横坐标
                canvas.drawText(time,x1 - lg/2 , lenghtY * marginY + startY,paint);

                //12小时
                if(mCountTdMap.containsKey(time)){
                    String value = mCountTdMap.get(time);
                    if(!TextUtils.isEmpty(value)){
                        Float fl  = Float.parseFloat(value);
                        if(fl < 30){
                            fl = 30f;
                        }else if(fl > 240){
                            fl = 240f;
                        }
                        cF.y = 16 * marginY + startY - (fl*marginY)/15;
//                        cF.y = Float.parseFloat(value);
                        mCountPointFList.add(cF);
                    }
                }
//                //单次
//                if(mSingleTdMap.containsKey(time)){
//                    ValuesBean<Integer,String> value = mSingleTdMap.get(time);
//
//                    String am = value.getValue2(0);     //早上
//
//                    if(!TextUtils.isEmpty(am)){
//                        Float fl  = Float.parseFloat(am);
//                        sF.y = 12 * marginY + startY - (fl*marginY)/2;
////                        sF.y = Float.parseFloat(value);
//                        mSinglePointFList.add(sF);
//                    }
//                }

                paint.setStrokeWidth(2);
            }


            paint.setColor(getResources().getColor(R.color.colorMain));

            canvas.drawLine(x1,startY,x1,(lenghtY -1)*marginY + startY,paint);
            paint.setStrokeWidth(1);
        }

        //画横线 和左右坐标
        for(int y = 0; y < (lenghtY ); y++){
//            paint.setColor(getResources().getColor(R.color.colorMain));
            //画横线
            float y1 = marginY * y + startY;

            paint.setColor(getResources().getColor(R.color.quickening_text1));
            if(y % 2 == 0 && y < lenghtY - 1){

                String tx1 = (240 - 30 * y/2) + "";
                String tx2 = (24 - 4 * y/2) + "";
                float lg = paint.measureText(tx1);
                float lg1 = paint.measureText(tx2);

                //左边坐标
                canvas.drawText((240 - 30 * y/2) + "",marginX + startX-lg-4,y1 + 5,paint);
                //右边坐标
                if(y > 0 && y < lenghtY - 2)
                    canvas.drawText((24 - 4 * y/2) + "",(lengthX -1) * marginX + lg1 + 4 ,y1 + 5,paint);


                paint.setStrokeWidth(2);
            }
            paint.setColor(getResources().getColor(R.color.colorMain));
            canvas.drawLine(0,y1,width,y1,paint);
            paint.setStrokeWidth(1);

        }

        //画单次标记,并画折线
        paint.setColor(getResources().getColor(R.color.quickening_line_o));
        for(int i = 0; i < mSinglePointFList.size(); i++){
            PointF p = mSinglePointFList.get(i);
            paint.setStrokeWidth(8);
            canvas.drawCircle(p.x,p.y,6,paint);
            if(i + 1 < mSinglePointFList.size()){
                PointF p1 = mSinglePointFList.get(i+1);
                paint.setStrokeWidth(2);
                canvas.drawLine(p.x,p.y,p1.x,p1.y,paint);
            }
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

    public void updataData(FetalMoveListsModel model , String currentTime){
//        FetalMovementListBakeModel dataWeekBeanList = fetalMovementList.get(position);
        mCountTdMap.clear();
        mSingleTdMap.clear();
        mSinglePointFList.clear();
        mCountPointFList.clear();
        if(model == null || TextUtils.isEmpty(currentTime)){
            return;
        }
        startTimePosition = TimeUtils.intervalDay(TimeUtils.getYMDData(0),currentTime);
        this.currentTime = currentTime;
        if(model.getRows() != null && model.getRows().size() > 0 ) {
            FetalMoveListsRowBean rowsBean = model.getRows().get(0);
            if(rowsBean != null){

                List<DateModelBean> dateModelBean = rowsBean.getDateModel();
                List<RecordTimePointModelBean> recordTimePointModelBeans = rowsBean.getRecordTimePointModel();
                for (DateModelBean dataWeekBean : dateModelBean) {
                    if (dataWeekBean != null) {
                        LogMessage.doLogMessage("BaseActivity"," fragment : " + dataWeekBean.getCreateTime());
                        String time = dataWeekBean.getCreateTime();
                        int day = TimeUtils.intervalDay(TimeUtils.getYMDData(0),time);
                        time = TimeUtils.getDate(-day);
                        mCountTdMap.put(time,dataWeekBean.getSUMClickNumber() + "");
//                        addSingleValue(time, TypeApi.TIME_IVTERVAL_AM, recordTimePointModelBeans.get(0)  + "");
//                        addSingleValue(time, TypeApi.TIME_IVTERVAL_PM, dataWeekBean.getAfternoon() + "");
//                        addSingleValue(time, TypeApi.TIME_IVTERVAL_NIGHT, dataWeekBean.getNight() + "");
                    }
                }

            }

            /**
             * 重新绘制
             */
//            postInvalidate();
            invalidate();
        }

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
