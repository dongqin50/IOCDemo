package com.conagra.mvp.utils;

/**
 * Created by Dongqin.Ye on 2018/6/16.
 */

public class SharedPreferencesUtils {

//    private final static String BLOOD_PRESSURE = "bloodPressure";
//    private final static String BLOOD_SUGAR = "bloodSugar";
//    private final static String FETAL_HEART = "fetalHeart";
//    private final static String TEMPLE = "temple";
//    private static  SharedPreferences DEVICES;
//    public static void init(Context context){
//        DEVICES = context.getSharedPreferences("devices",MODE_PRIVATE);
//    }
//
//    public static boolean isExistAboutBloodPressure(String mac){
//        if(!TextUtils.isEmpty(mac)){
//            String bloodPressure = getBloodPressure();
//            if(!TextUtils.isEmpty(bloodPressure)){
//                return bloodPressure.contains(mac);
//            }
//        }
//        return false;
//    }
//
//    public static void addBloodPressure(String mac){
//        if(!isExistAboutBloodPressure(mac)){
//            String bloodPressure = getBloodPressure();
//            if(TextUtils.isEmpty(bloodPressure)){
//                bloodPressure = mac + ",";
//            }else {
//                bloodPressure = bloodPressure + "," + mac;
//            }
//            SharedPreferences.Editor editor = DEVICES.edit();
//            editor.putString(BLOOD_PRESSURE,bloodPressure);
//            editor.commit();
//        }
//    }
//
//    public static boolean isExistAboutBloodSugar(String mac){
//        if(!TextUtils.isEmpty(mac)){
//            String bloodSugar = getBloodSugar();
//            if(!TextUtils.isEmpty(bloodSugar)){
//
//                return bloodSugar.contains(mac);
//            }
//        }
//        return false;
//    }
//
//    public static void addBloodSugar(String mac){
//        if(!isExistAboutBloodSugar(mac)){
//            String bloodSugar = getBloodSugar();
//            if(TextUtils.isEmpty(bloodSugar)){
//                bloodSugar = mac + ",";
//            }else {
//                bloodSugar = bloodSugar + "," + mac;
//            }
//            SharedPreferences.Editor editor = DEVICES.edit();
//            editor.putString(BLOOD_SUGAR,bloodSugar);
//            editor.commit();
//        }
//    }
//
//    public static boolean isExistAboutTemple(String mac){
//        if(!TextUtils.isEmpty(mac)){
//            String temple = getTemple();
//            if(!TextUtils.isEmpty(temple)){
//
//                return temple.contains(mac);
//            }
//        }
//        return false;
//    }
//
//    public static void addTemple(String mac){
//        if(!isExistAboutTemple(mac)){
//            String temple = getTemple();
//            if(TextUtils.isEmpty(temple)){
//                temple = mac + ",";
//            }else {
//                temple = temple + "," + mac;
//            }
//            SharedPreferences.Editor editor = DEVICES.edit();
//            editor.putString(TEMPLE,temple);
//            editor.commit();
//        }
//    }
//
//    public static boolean isExistAboutFetalHeart(String mac){
//        if(!TextUtils.isEmpty(mac)){
//            String fetalHeart = getFetalHeart();
//            if(!TextUtils.isEmpty(fetalHeart)){
//
//                return fetalHeart.contains(mac);
//            }
//        }
//        return false;
//    }
//
//    public static void addFetalHeart(String mac){
//        if(!isExistAboutBloodPressure(mac)){
//            String fetalHeart = getFetalHeart();
//            if(TextUtils.isEmpty(fetalHeart)){
//                fetalHeart = mac + ",";
//            }else {
//                fetalHeart = fetalHeart + "," + mac;
//            }
//            SharedPreferences.Editor editor = DEVICES.edit();
//            editor.putString(FETAL_HEART,fetalHeart);
//            editor.commit();
//        }
//    }
//
//    public static String getBloodPressure(){
//        return DEVICES.getString(BLOOD_PRESSURE,"");
//    }
//
//    public static String getBloodSugar(){
//        return DEVICES.getString(BLOOD_SUGAR,"");
//    }
//
//    public static String getTemple(){
//        return DEVICES.getString(TEMPLE,"");
//    }
//
//    public static String getFetalHeart(){
//        return DEVICES.getString(FETAL_HEART,"");
//    }
//
//
//
//


}
