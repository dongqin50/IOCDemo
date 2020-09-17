package com.zhy.iocdemo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class PluginManager {

//    private PackageInfo

    private static PluginManager mPluginManager = new PluginManager();
    private String mPluginName = "plugin";
    private PackageInfo mPackageInfo;
    private Resources mExtraResource;
    private DexClassLoader mDexClassLoader;

    public static PluginManager getInstance() {
        return mPluginManager;
    }

//    public ClassLoader getExtraClassLoader(String path) {
//
//        try {
//            AssetManager assetManager = AssetManager.class.newInstance();
//            Method addAssetPathMethod = AssetManager.class.getMethod("addAssetPath", String.class);
//            addAssetPathMethod.invoke(assetManager, path);
//
//
//            DexClassLoader dexClassLoader = new DexClassLoader();
//
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }


    public PackageInfo getPackageInfo() {
        return mPackageInfo;
    }

    public Resources getExtraResource() {
        return mExtraResource;
    }

    public DexClassLoader getDexClassLoader() {
        return mDexClassLoader;
    }

    public String loadExtraApk(Context context, String name, boolean update) {
        String pluginPath = copyPluginApk2PirvateDir(context, name, update);
        File file = new File(pluginPath);
        if(!file.exists()){
            return "";
        }
        if (TextUtils.isEmpty(pluginPath)) return "";
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, pluginPath);

            mExtraResource = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());

            File dexFile = context.getDir("dex", Context.MODE_PRIVATE);

            mDexClassLoader = new DexClassLoader(pluginPath, dexFile.getAbsolutePath(), null, context.getClassLoader());
            PackageManager pm = context.getPackageManager();
            mPackageInfo = pm.getPackageArchiveInfo(pluginPath, PackageManager.GET_ACTIVITIES|PackageManager.GET_SERVICES);
            if (mPackageInfo != null && mPackageInfo.activities.length > 0) {
                return mPackageInfo.activities[0].name;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return "";


    }

    //将外部插件拷贝到私有目录中
    public String copyPluginApk2PirvateDir(Context context, String name, boolean update) {

        File localFile = new File(Environment.getExternalStorageDirectory() + "/" + name);
        if (!localFile.exists()) {
            return "";
        }

        File file = context.getDir(mPluginName, Context.MODE_PRIVATE);
        String filePath = new File(file, name).getAbsolutePath();
        File exportFile = new File(filePath);
        if (exportFile.exists()) {
            if (update) {
                exportFile.delete();
            } else {
                return filePath;
            }
        }

        try {
            copyFile(localFile, exportFile);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        if (exportFile.exists()) {
            return filePath;
        }
        return "";

    }

    private void copyFile(File fromFile, File toFile) throws IOException {
        FileOutputStream output = null;
        FileInputStream inputStream = null;

        try {
            output = new FileOutputStream(toFile);
            inputStream = new FileInputStream(fromFile);

            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
            output.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
