package com.zy.skin_update.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class ResourceUtils {



    /**
     * 获取额外apk资源
     *
     * @param path
     * @param resources
     * @return
     */
    public static Resources getExtraApkResource(String path, Resources resources) {
        try {

            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(assetManager, path);

            return new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String  getExtraPackageName(Application application, String path) {

        PackageManager pm = application.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if(packageInfo != null){
            return packageInfo.packageName;
        }
        return "";
    }

    public static int getExtraResId(Resources resources,int resId,Resources extraResource,String packageName){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(resources," resources is null");
            Objects.requireNonNull(extraResource,"extraResource is null");
        }else {
            Objects.requireNonNull(resources);
            Objects.requireNonNull(extraResource);
        }

        if(resources.equals(extraResource)){
            return -1;
        }

        String resName = resources.getResourceEntryName(resId);
        String resType = resources.getResourceTypeName(resId);
        return extraResource.getIdentifier(resName,resType,packageName);
    }




    public static int[] getResIdByAttrsId(Context context,int[] attrs){

        //获取application中的主题attr
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int[] resIds= new int[attrs.length];
        for(int i = 0; i < attrs.length; i++){
            resIds[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        return resIds;
    }




}
