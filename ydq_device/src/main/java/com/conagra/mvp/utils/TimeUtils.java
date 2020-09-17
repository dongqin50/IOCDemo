package com.conagra.mvp.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yedongqin on 16/9/23.
 *
 * 时间工具
 */
public class TimeUtils {
    
    private static final long INIT_TIME = 1474560000000l; //2016-09-23日,周五
    private static final long DAY = 1000 * 60 *60 *24;

    /**
     * 返回月-日
     * @param num
     * @return
     */
    public static String getDate(long num){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        Date date = calendar.getTime();
        String data = format.format(date);
        date.setTime(date.getTime() + DAY*num);
        data = format.format(date);

        return data;
    }


    public static String getTime(String time,String fromFormat,String ToFormat){
        time = update(time);
        if(TextUtils.isEmpty(time)){
            return null;
        }

        SimpleDateFormat fromDateFormat = new SimpleDateFormat(fromFormat);
        SimpleDateFormat toDateFormat = new SimpleDateFormat(ToFormat);
        try {
            Date date = fromDateFormat.parse(time);
            return toDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取年月日
     * @param num
     * @return
     */
    public static String getYMDData(long num){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String data = format.format(date);
        date.setTime(date.getTime() + DAY*num);
        data = format.format(date);
        return data;
    }
    /**
     * 获取年月日
//     * @param dateString
     * @return
     */
//    public static String getYMDDataString(String dateString){
//
//        if(TextUtils.isEmpty(dateString)){
//            return null;
//        }
//
//        return dateString.split("T")[0];
//    }

    public static Date getYMDData(String date,String formatString){

        date = update(date);
        if(TextUtils.isEmpty(date)){
            return null;
        }

        SimpleDateFormat format = new SimpleDateFormat(formatString);
        try {
            Date date1 = format.parse(date);
            return date1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间比较器
     * @param time1
     * @param time2
     * @param formatString  格式
     * @return
     *         -1: 解析失败,无法进行比较
     *         0 : time1 < time2
     *         1 : time1 = time2
     *         2 : time1 > time2
     */
    public static int compartorTime(String time1,String time2,String formatString){


        int result = -1;
        time1 = update(time1);
        time2 = update(time2);
        if(TextUtils.isEmpty(time1) || TextUtils.isEmpty(time2) || TextUtils.isEmpty(formatString)){
            return result;
        }

        SimpleDateFormat format = new SimpleDateFormat(formatString);
        try {
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);
            result = date1.compareTo(date2);
            if(result == 0){
                result = 1;
            }else if(result < 0){
                result = 0;
            }else{
                result = 2;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            result = -1;
        }
        return result;
    }

    /**
     * 时间间隔 s
     * @param time1
     * @param time2
     * @return
     */
    public static int intervalTime(String time1,String time2){

       return intervalTime(time1,time2,"yyyy-MM-dd HH:mm:ss");
    }
    /**
     * 时间间隔 s
     * @param time1
     * @param time2
     * @return
     */
   public static int intervalTime(String time1,String time2,String formatString){

        time1 = update(time1);
        time2 = update(time2);
        if(TextUtils.isEmpty(time1) || TextUtils.isEmpty(time2)){
            return -1;
        }

        if(time1.contains(".")){
            time1 = time1.split("\\.")[0];
        }

        SimpleDateFormat format = new SimpleDateFormat(formatString);
        try {
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);

            long time = Math.abs(date1.getTime() - date2.getTime());
            return (int) (time/1000);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String update(String time){
        if(TextUtils.isEmpty(time)){
            return null;
        }
        if(time.contains(".")){
            time = time.split("\\.")[0];
        }

        if(time.contains("T")){
            time = time.replace("T"," ");
        }
        return time;
    }

    /**
     * 时间间隔 s
     * @param time1
     * @param time2
     * @return
     */
    public static int intervalDay(String time1,String time2){

        time1 = update(time1);
        time2 = update(time2);
        if(TextUtils.isEmpty(time1) || TextUtils.isEmpty(time2)){
            return -1;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);

            long time = date1.getTime() - date2.getTime();
            return (int) (time / DAY);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 时间间隔 s
     * @param time1
     * @param time2
     * @return
     */
    public static int intervalTimeByMs(String time1,String time2){
        time1 = update(time1);
        time2 = update(time2);
        if(TextUtils.isEmpty(time1) || TextUtils.isEmpty(time2)){
            return -1;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);

            long time = Math.abs(date1.getTime() - date2.getTime());
            return Integer.parseInt(Long.valueOf(time).toString()) / 1000;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 时间间隔 s
     * @param time1
     * @param time2
     * @return
     */
    public static int intervalTimePN(String time1,String time2){
        time1 = update(time1);
        time2 = update(time2);
        if(TextUtils.isEmpty(time1) || TextUtils.isEmpty(time2)){
            return -1;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);

            long time = date1.getTime() - date2.getTime();
            return Integer.parseInt(Long.valueOf(time).toString()) / 1000;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取时间距离当前的天数
     * @param time
     * @return
     *          0 : time <=
     *          > 0 : time >
     *          -1 : 异常
     */
//    public static int getCountData(String time,int intervalTime){
//        int it = getCountData(time)
//    }
    public static long getCountData(String time){

        time = update(time);
        if(TextUtils.isEmpty(time)){
            return -1;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String current = getYMDData(0);
        try {
            Date date1 = format.parse(time);
            Date date2 = format.parse(current);
            long day = (date1.getTime() - date2.getTime())/DAY;
//            if(date1.getTime()>date1.getTime()){
//                int day = (int) ((date1.getTime() - date2.getTime())/DAY);
//                return day;
//            }else {
//                int day = (int) ((date2.getTime() - date1.getTime())/DAY);
//                return -day;
//            }
            return day;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }


    /**
     * 获取此刻的时间
     *
     * @return
     */
    public static String getCurrentTime(){

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String data = format.format(date);

        return data;
    }

    /**
     * 获取此刻的时间
     *
     * @return
     */
    public static String getCurrentTime(String... formatString){

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String data = "";
        for(String s : formatString){
            SimpleDateFormat format = new SimpleDateFormat(s);
            data += format.format(date);

        }

        return data;
    }

    /**
     * 获取此刻的时间
     *
     * @return
     */
    public static String  getCurrentTimeByDate(long time){

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        date.setTime(date.getTime() +time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String data = format.format(date);
//        try {
//            date = format.parse(data);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        return data;
    }

    /**
     * 获取此刻的时间
     *
     * @return
     */
    public static String  doChangeFormat(Date date,String formatStr){
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        String data = format.format(date);
        return data;
    }


    /**
     * 获取此刻的时间
     *
     * @return
     */
    public static String  getTimeByDate(long time){

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        date.setTime(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String data = format.format(date);
//        try {
//            date = format.parse(data);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        return data;
    }

    /**
     * 获取此刻的时间
     *
     * @return
     */
    public static String  getTimeFromYmdHmsSToYmdHmsDate(String time){
        time = update(time);
        if(TextUtils.isEmpty(time)){
            return null;
        }
        return time;
    }
    /**
     * 获取此刻的时间
     *
     * @return
     */
    public static String getCurrentTimeByHM(long time){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String data = format.format(date);

        return data;
    }

    public static String getTime(long time,String stringFormat){
        Date date = new Date(time);
        SimpleDateFormat fromFormat = new SimpleDateFormat(stringFormat);
        String dateString = fromFormat.format(date);
        return dateString;
    }

    public static long getTime(String time,String stringFormat){

        SimpleDateFormat fromFormat = new SimpleDateFormat(stringFormat);
        try {
            Date date = fromFormat.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取此刻的时间
     *
     * @return
     */
    public static long getHMStringToLong(String time){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
          Date data = format.parse(time);
            return data.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取此刻的时间 mm:ss
     *
     * @return
     */
    public static String getCurrentTimeByMS(long time){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        String data = format.format(date);

        return data;
    }
    /**
     * 获取此刻的时间 mm
     *
     * @return
     */
    public static String getCurrentTimeByMM(long time){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("mm");
        String data = format.format(date);

        return data;
    }



    /**
     * 获取此刻的时间
     *
     * @return
     */
    public static long getCurrentTimeStringByHM(String time){
        time = update(time);
        if(TextUtils.isEmpty(time)){
            return -1;
        }
        long longTime = 0;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date data = format1.parse(time);
            String dataString = format.format(data);
            data = format.parse(dataString);
            longTime = data.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return longTime;
    }

    /**
     * 周期
     * yyyy-MM-dd
     * @param
     * @return
     */
    public static String getWeek(int num){

        Calendar calendar = Calendar.getInstance();
        long date = calendar.getTimeInMillis()+DAY*num;
       long time = date - INIT_TIME;
       int day = (int) ((time/DAY + 5)%7);
        String data = "";
        switch (Math.abs(day)){
            case 0:
                data = "周日";
                break;
            case 1:
                data = "周一";
                break;
            case 2:
                data = "周二";
                break;
            case 3:
                data = "周三";
                break;
            case 4:
                data = "周四";
                break;
            case 5:
                data = "周五";
                break;
            case 6:
                data = "周六";
                break;
        }
        
        return data;
    }

}
