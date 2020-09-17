package com.conagra.hardware.model;

import android.support.annotation.DrawableRes;

import com.conagra.mvp.constant.TypeApi;

import java.io.Serializable;

/**
 * Created by yedongqin on 16/9/15.
 */
public class JkgjModel implements Serializable {

    public JkgjModel() {
    }

    public JkgjModel(int resId, String content, int type, boolean isAdd, boolean isCode) {
        this.resId = resId;
        this.isAdd = isAdd;
        this.type = type;
        if(isCode){
            this.code = content;
            this.content = getCodeContent();
        }else {
            this.content = content;
            this.code = getContentCode();
        }

    }
    public JkgjModel(int resId, String content, boolean isCode) {
        this(resId,content,0,false,isCode);
    }
    public JkgjModel(int resId, String content, int type) {
        this(resId,content,type,false,false);
    }

    public JkgjModel(int resId, String content, boolean isAdd, int type) {
       this(resId,content,0,isAdd,false);
    }

    public JkgjModel(int resId, String content) {
        this(resId,content,0,false,false);
    }





    /**
     * 图片
     */
    private @DrawableRes
    int resId;

    /**
     * 内容
     */
    private String content;

    /**
     * 判断是否添加
     */
    private boolean isAdd;

    private int type;       //健康工具，0  频道订阅  1

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public String getContentCode(){
        switch (content){
            case  TypeApi.HARDWARE_STRING_FETALHEART:           // "胎心";// FetalHeart（胎心仪）
                return TypeApi.HARDWARE_FETALHEART;
            case  TypeApi.HARDWARE_STRING_FETALMOVEMENT: // "胎动";// FetalMovement（胎动）
                return TypeApi.HARDWARE_FETALMOVEMENT; // "胎动";// FetalMovement（胎动）
            case  TypeApi.HARDWARE_STRING_BLOODPRESSURE:// "血压";//BloodPressure（血压仪）
                return  TypeApi.HARDWARE_BLOODPRESSURE;// "血压";//BloodPressure（血压仪）
            case  TypeApi.HARDWARE_STRING_BLOODSUGAR:   // "血糖";//BloodSugar（血糖仪器）
                return  TypeApi.HARDWARE_BLOODSUGAR;   // "血糖";//BloodSugar（血糖仪器）
            case  TypeApi.HARDWARE_STRING_BLOODFAT: // "血脂";//BloodFat（血脂）
                return  TypeApi.HARDWARE_BLOODFAT; // "血脂";//BloodFat（血脂）
            case  TypeApi.HARDWARE_STRING_ELECTROCARDIOGRAM:   // "心电";//Electrocardiogram（心电图）
                return  TypeApi.HARDWARE_ELECTROCARDIOGRAM;   // "心电";//Electrocardiogram（心电图）
            case  TypeApi.HARDWARE_STRING_TEMPERATURE:         // "体温";//Temperature（体温）
                return  TypeApi.HARDWARE_TEMPERATURE;         // "体温";//Temperature（体温）
            case  TypeApi.HARDWARE_STRING_URINE:       // "尿液";//Urine（尿液）
                return  TypeApi.HARDWARE_URINE;       // "尿液";//Urine（尿液）
            case  TypeApi.HARDWARE_STRING_OXYGEN:      // "血氧";//Oxygen（血氧）
                return  TypeApi.HARDWARE_OXYGEN;      // "血氧";//Oxygen（血氧）
            case  TypeApi.HARDWARE_STRING_HEARTRATE:       // "心率";//HeartRate（心率)
                return  TypeApi.HARDWARE_HEARTRATE;       // "心率";//HeartRate（心率)
            case  TypeApi.HARDWARE_STRING_WEIGHT:      // "体重";//Weight（体重）
                return  TypeApi.HARDWARE_WEIGHT;      // "体重";//Weight（体重）

        }
        return null;
    }
    public String getCodeContent(){
        switch (code){
            case  TypeApi.HARDWARE_FETALHEART:           // "胎心";// FetalHeart（胎心仪）
                return TypeApi.HARDWARE_STRING_FETALHEART;
            case  TypeApi.HARDWARE_FETALMOVEMENT: // "胎动";// FetalMovement（胎动）
                return TypeApi.HARDWARE_STRING_FETALMOVEMENT; // "胎动";// FetalMovement（胎动）
            case  TypeApi.HARDWARE_BLOODPRESSURE:// "血压";//BloodPressure（血压仪）
                return  TypeApi.HARDWARE_STRING_BLOODPRESSURE;// "血压";//BloodPressure（血压仪）
            case  TypeApi.HARDWARE_BLOODSUGAR:   // "血糖";//BloodSugar（血糖仪器）
                return  TypeApi.HARDWARE_STRING_BLOODSUGAR;   // "血糖";//BloodSugar（血糖仪器）
            case  TypeApi.HARDWARE_BLOODFAT: // "血脂";//BloodFat（血脂）
                return  TypeApi.HARDWARE_STRING_BLOODFAT; // "血脂";//BloodFat（血脂）
            case  TypeApi.HARDWARE_ELECTROCARDIOGRAM:   // "心电";//Electrocardiogram（心电图）
                return  TypeApi.HARDWARE_STRING_ELECTROCARDIOGRAM;   // "心电";//Electrocardiogram（心电图）
            case  TypeApi.HARDWARE_TEMPERATURE:         // "体温";//Temperature（体温）
                return  TypeApi.HARDWARE_STRING_TEMPERATURE;         // "体温";//Temperature（体温）
            case  TypeApi.HARDWARE_URINE:       // "尿液";//Urine（尿液）
                return  TypeApi.HARDWARE_STRING_URINE;       // "尿液";//Urine（尿液）
            case  TypeApi.HARDWARE_OXYGEN:      // "血氧";//Oxygen（血氧）
                return  TypeApi.HARDWARE_STRING_OXYGEN;      // "血氧";//Oxygen（血氧）
            case  TypeApi.HARDWARE_HEARTRATE:       // "心率";//HeartRate（心率)
                return  TypeApi.HARDWARE_STRING_HEARTRATE;       // "心率";//HeartRate（心率)
            case  TypeApi.HARDWARE_WEIGHT:      // "体重";//Weight（体重）
                return  TypeApi.HARDWARE_STRING_WEIGHT;      // "体重";//Weight（体重）

        }
        return null;
    }
}
