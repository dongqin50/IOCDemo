package com.conagra.mvp.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.view.WindowManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bob on 2015/2/2.
 */
public class CommonUtils {

    /**
     * 得到屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Activity context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;

    }

    /**
     * 得到屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Activity context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    /**
     * 得到状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Activity context) {
        int statusHeight = 0;
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusHeight = frame.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * 获得当前的版本信息
     *
     * @return
     */
    public static String[] getVersionInfo(Context context) {
        String[] version = new String[2];

        PackageManager packageManager = context.getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version[0] = String.valueOf(packageInfo.versionCode);
            version[1] = packageInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    /**
     * 邮箱格式是否正确
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {

        if (TextUtils.isEmpty(email))
            return false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        if (matcher.matches())
            return true;
        else
            return false;

    }

    /**
     * 是否开启 GPS
     *
     * @param context
     * @return
     */
    public static boolean isOpenGPS(Context context) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNPEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnable || isNPEnable)
            return true;


        return false;
    }

    public static void openGPS(Context context) {

        Intent GPSIntent = new Intent();

        GPSIntent.setClassName("com.android.settings",
                               "com.android.settings.widget.SettingsAppWidgetProvider");

        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));

        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否是手机号
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches() ) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 判断是否为空
     * @param value
     * @return
     */
    public static boolean isNumber(String value){
//        Pattern.matches("^(0|[1-9]+\\.[0-9]+)|0|[1-9]+|([1-9]+\\.[0-9]+)$",value)
        return Pattern.matches("^[0-9]+\\.?[0-9]*$",value);
    }

//    public static String getCheckCode(String uid,String pwd){

//        String  result;
//
//         pwd = MD5(pwdVal);	    //密码MD5
//         uid = uidVal;			//用户名
//        string str1 = telNumber;  //手机号
//        string str = content; //内容
//        String productidVal = "48661";
//        StringBuilder sbTemp = new StringBuilder();
//        sbTemp.Append("username=" + uid + "&password=" + pwd + "&mobile=" + str1 + "&content=" + str + "&dstime=&productid=" + productidVal + "&xh=");
//        byte[] bTemp = System.Text.Encoding.GetEncoding("UTF-8").GetBytes(sbTemp.ToString());
//        string postReturn = SMS.doPostRequest("http://www.ztsms.cn:8800/sendSms.do", bTemp);
//
//        if (postReturn.IndexOf(",") >= 0)
//            postReturn = postReturn.Split(',')[0];
//
//        if (postReturn == "1")
//        {
//            result = "0";       //成功
//        }
//        else
//        {
//            result = "-1";  //失败
//        }

//    }


    /**
     * 检查是否有网络连接
     * @return 有返回true，没有返回false
     */
    public static boolean isConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null&& info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }
}
