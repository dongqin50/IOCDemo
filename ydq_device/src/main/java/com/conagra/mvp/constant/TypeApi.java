package com.conagra.mvp.constant;

/**
 * Created by yedongqin on 2016/10/11.
 */
public interface TypeApi {

    //0:取消关注  1: 关注
    int ATENTION_DOCTOR_CANCLE = 0;
    int ATENTION_DOCOTR = 1;

    int EVALUATE_DOCTOR_UN_REVIEWED = 0;        //未审核
    int EVALUATE_DOCTOR_IS_REVIEWED = 1;        //审核

    int TIME_IVTERVAL_AM = 0;       //上午
    int TIME_IVTERVAL_PM = 1;       // 下午
    int TIME_IVTERVAL_NIGHT = 2;   //晚上

    String PAY_MODE_ALIPAY = "支付宝";
    String PAY_MODE_WEI_XIN = "微信";
    String PAY_MODE_E_BANK = "网银";

    /// * Category值
    /// *	0：早餐前	1：早餐后
    /// *	2：午餐前	3：午餐后
    /// *	4：晚餐前	5：晚餐后
    /// *	6：睡觉前

    int CATEGORY_AM_BEFORE = 0;
    int CATEGORY_AM_AFTER = 1;
    int CATEGORY_PM_BEFORE = 2;
    int CATEGORY_PM_AFTER = 3;
    int CATEGORY_NIGHT_BEFORE = 4;
    int CATEGORY_NIGHT_AFTER = 5;
    int CATEGORY_SLEEP_BEFORE = 6;

    int CATEGORY_AM = 0;
    int CATEGORY_PM = 1;
    int CATEGORY_NIGHT = 2;
    int CATEGORY_SLEEP = 3;



//    文件类型 0：胎心音频、   1：自助咨询图片
    int UPLOAD_FILETYPE_VIDEO = 0;
    int UPLOAD_FILETYPE_IMAGE = 1;


    String BLOODPRESSURE_STATE_STRING_ZERO = "正常血压";
    String BLOODPRESSURE_STATE_STRING_ONE = "正常高值";
    String BLOODPRESSURE_STATE_STRING_TWO = "高血压";
    String BLOODPRESSURE_STATE_STRING_THREE = "1级高血压（轻度)";
    String BLOODPRESSURE_STATE_STRING_FOUR = "2级高血压（中度）";
    String BLOODPRESSURE_STATE_STRING_FIVE = "3级高血压（重度）";
    String BLOODPRESSURE_STATE_STRING_SIX = "单纯收缩期高血压";
    String BLOODPRESSURE_STATE_STRING_SERVEN = "低血压";
    String BLOODPRESSURE_STATE_STRING_EIGHT = "脉压差减小（正常）";
    String BLOODPRESSURE_STATE_STRING_NINE = "脉压差增大（正常）";
    String BLOODPRESSURE_STATE_STRING_TEN = "脉压差减小（高血压）";
    String BLOODPRESSURE_STATE_STRING_ELEVEN = "脉压差增大（高血压）";
    String BLOODPRESSURE_STATE_STRING_TWELVE = "脉压差减小（低血压）";
    String BLOODPRESSURE_STATE_STRING_THIRTEEN = "脉压差增大（低血压）";
    

    /**
     * [State]血压状态值
     ====================================================================
     0：正常血压
     1：正常高值
     2：高血压
     3：1级高血压（轻度）
     4：2级高血压（中度）
     5：3级高血压（重度）
     6：单纯收缩期高血压
     7：低血压
     8：脉压差减小（正常）
     9：脉压差增大（正常）
     10：脉压差减小（高血压）
     11：脉压差增大（高血压）
     12：脉压差减小（低血压）
     13：脉压差增大（低血压）
     */
      int BLOODPRESSURE_STATE_ZERO = 0;
      int BLOODPRESSURE_STATE_ONE = 1;
      int BLOODPRESSURE_STATE_TWO = 2;
      int BLOODPRESSURE_STATE_THREE = 3;
      int BLOODPRESSURE_STATE_FOUR = 4;
      int BLOODPRESSURE_STATE_FIVE = 5;
      int BLOODPRESSURE_STATE_SIX = 6;
      int BLOODPRESSURE_STATE_SERVEN = 7;
      int BLOODPRESSURE_STATE_EIGHT = 8;
      int BLOODPRESSURE_STATE_NINE = 9;
      int BLOODPRESSURE_STATE_TEN = 10;
      int BLOODPRESSURE_STATE_ELEVEN = 11;
      int BLOODPRESSURE_STATE_TWELVE = 12;
      int BLOODPRESSURE_STATE_THIRTEEN = 13;

    /**
     * 脉压,是指收缩压(简称高压)减去舒张压(简称低压)的数值。
     1、正常人的脉压差为20~60毫米汞柱(2.67~8.0Kpa)
     2、小于20mmHg：脉压差减小
     3、大于60mmHg：脉压差增大

     高血压
     ====================================================================
     收缩压                     舒张压
     正常血压                        ≥90 && <120   和          <80
     正常高值						120～139       或		   80～89
     高血压                          ≥140          或		   ≥90
     1级高血压（轻度）				140～159       或		   90～99
     2级高血压（中度）				160～179       或		   100～109
     3级高血压（重度）				≥180          或          ≥110
     单纯收缩期高血压				≥140          和          <90
     低血压
     ====================================================================
     收缩压                     舒张压
     <90						   <60

     */

    String  BLOODPRESSURE_TYPE_NORMAL = "正常血压";
    String  BLOODPRESSURE_TYPE_GXY_NORMAL = "正常高值";
    String  BLOODPRESSURE_TYPE_GXY = "高血压";
    String  BLOODPRESSURE_TYPE_GXY_ONE = "1级高血压（轻度）";
    String  BLOODPRESSURE_TYPE_GXY_TWO = "2级高血压（中度）";
    String  BLOODPRESSURE_TYPE_GXY_THREE = "3级高血压（重度）";
    String  BLOODPRESSURE_TYPE_GXY_DCSSQ = "单纯收缩期高血压";
    String  BLOODPRESSURE_TYPE_DXY = "低血压";


    int KESHI_INDEX = 1;
    int KESHI_CONSULT_FAST = 2;
    int KESHI_CONSULT_EXPERT_PICTURE = 3;
    int KESHI_CONSULT_EXPERT_MOBILE = 4;
    int KESHI_CONSULT_EXPERT_VIDEO = 5;

    int PAY_CONSULT_FREE = 0;
    int PAY_CONSULT_FAST = 1;
    int PAY_CONSULT_EXPERT_PICTURE = 2;
    int PAY_CONSULT_EXPERT_MOBILE = 3;
    int PAY_CONSULT_EXPERT_VIDEO = 4;


    int MESSAGE_CONSULT_FREE = 0;
    int MESSAGE_CONSULT_FAST = 1;
    int MESSAGE_EXPERT_PICTURE = 2;
    int MESSAGE_EXPERT_MOBILE = 3;
    int MESSAGE_EXPERT_VIDEO = 4;



     int STOP_STATUS_BACK  = 0;
     int STOP_STATUS_NORMAL  = 1;
     int STOP_STATUS_FORCE = 2;
    //血糖设备mac地址
//    String BLUETOOTH_DEVICE_MAC_BLOOD_SUGAR = "C6:05:04:03:09:AE";

    //血压设备mac地址
//    String BLUETOOTH_DEVICE_MAC_BLOOD_PRESSURE = "06:05:04:03:02:D1";

    //客户那边的设备
    String BLUETOOTH_DEVICE_MAC_BLOOD_SUGAR = "C6:05:04:03:3C:75";
    String BLUETOOTH_DEVICE_MAC_BLOOD_PRESSURE = "C6:05:04:03:05:58";
//    String BLUETOOTH_DEVICE_MAC_BLOOD_SUGAR = "C6:05:04:03:09:AE";


    int REQUEST_CODE_1 = 0;

    //设备编码：
    String HARDWARE_FETALHEART = "FETALHEART";// FetalHeart（胎心仪）
    String HARDWARE_FETALMOVEMENT = "FETALMOVEMENT";// FetalMovement（胎动）
    String HARDWARE_BLOODPRESSURE = "BLOODPRESSURE";//BloodPressure（血压仪）
    String HARDWARE_BLOODSUGAR = "BLOODSUGAR";//BloodSugar（血糖仪器）
    String HARDWARE_BLOODFAT = "BLOODFAT";//BloodFat（血脂）
    String HARDWARE_ELECTROCARDIOGRAM = "ELECTROCARDIOGRAM";//Electrocardiogram（心电图）
    String HARDWARE_TEMPERATURE = "TEMPERATURE";//Temperature（体温）
    String HARDWARE_URINE = "URINE";//Urine（尿液）
    String HARDWARE_OXYGEN = "OXYGEN";//Oxygen（血氧）
    String HARDWARE_HEARTRATE = "HEARTRATE";//HeartRate（心率)
    String HARDWARE_WEIGHT = "WEIGHT";//Weight（体重）

    String HARDWARE_STRING_FETALHEART = "胎心";// FetalHeart（胎心仪）
    String HARDWARE_STRING_FETALMOVEMENT  = "胎动";// FetalMovement（胎动）
    String HARDWARE_STRING_BLOODPRESSURE  = "血压";//BloodPressure（血压仪）
    String HARDWARE_STRING_BLOODSUGAR  = "血糖";//BloodSugar（血糖仪器）
    String HARDWARE_STRING_BLOODFAT  = "血脂";//BloodFat（血脂）
    String HARDWARE_STRING_ELECTROCARDIOGRAM  = "心电";//Electrocardiogram（心电图）
    String HARDWARE_STRING_TEMPERATURE  = "体温";//Temperature（体温）
    String HARDWARE_STRING_URINE  = "尿液";//Urine（尿液）
    String HARDWARE_STRING_OXYGEN  = "血氧";//Oxygen（血氧）
    String HARDWARE_STRING_HEARTRATE  = "心率";//HeartRate（心率)
    String HARDWARE_STRING_WEIGHT  = "体重";//Weight（体重）


//    int MESSAGE_JSTX = 1;      //IM即时通讯
//    int MESSAGE_YY = 1;     //预约成功

    int MESSAGE_CONTENT = 1;      //文本类型
    int MESSAGE_PIC = 2;     //图片类型





}
