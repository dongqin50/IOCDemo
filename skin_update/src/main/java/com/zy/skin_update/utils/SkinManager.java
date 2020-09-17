package com.zy.skin_update.utils;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.zy.skin_update.SkinAttrs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 皮肤更新
 */
public class SkinManager {

    //默认皮肤
    private boolean isDefaultSkin;
    private Resources mDefaultResourceSkin;    //默认
    private Resources mCurrentResourceSkin; //当前资源
    private static SkinManager mSkinManager;
    private String mExtraResourcePackageName;
    private Map<String, Resources> mExtraSkinMap = new HashMap<>();
    private Application mApplication;
    private Map<String, Map<View, SkinAttrs>> mActivityManager = new HashMap<>();
    private static final List<String> mAttributes = new ArrayList<>();

    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
    }

    private SkinManager() {
    }

    public Resources getDefaultResourceSkin() {
        return mDefaultResourceSkin;
    }

    public Resources getCurrentResourceSkin() {
        return mCurrentResourceSkin;
    }

    public boolean isDefaultSkin() {
        return isDefaultSkin;
    }


    public static SkinManager getInstance() {
        return mSkinManager;
    }

    public static SkinManager newInstance(Application application, Resources defaultResource) {

        if (null == mSkinManager) {
            if (application == null) {
                throw new NullPointerException("newInstance(Context context) ,context is null");
            }
            synchronized (SkinManager.class) {
                if (mSkinManager == null) {
                    mSkinManager = new SkinManager();
                    mSkinManager.mApplication = application;
                    mSkinManager.mDefaultResourceSkin = application.getResources();
                    mSkinManager.mCurrentResourceSkin = defaultResource;
                    mSkinManager.isDefaultSkin = Objects.equals(mSkinManager.mDefaultResourceSkin, mSkinManager.mCurrentResourceSkin);
                }
            }
        }
        return mSkinManager;
    }

    /**
     * 还原为默认资源
     */
    public void resetSkin() {
        if (!isDefaultSkin) {
            isDefaultSkin = true;
            mCurrentResourceSkin = mDefaultResourceSkin;
            mExtraResourcePackageName = "";
            updateApply();
        }
//        return mCurrentResourceSkin;

    }

    public void loadExtraResource(String path) {
        Objects.requireNonNull(path);
        Resources extraResource;
        if (mExtraSkinMap.containsKey(path)) {
            extraResource = mExtraSkinMap.get(path);
        } else {
            extraResource = ResourceUtils.getExtraApkResource(path, mApplication.getResources());
            if (extraResource != null) {
                mExtraSkinMap.put(path, extraResource);
            }
        }

        if(!mCurrentResourceSkin.equals(extraResource)){
            if (extraResource != null) {
                mCurrentResourceSkin = extraResource;
                isDefaultSkin = false;
                mExtraResourcePackageName = ResourceUtils.getExtraPackageName(mApplication, path);
                updateApply();
            }

        }

    }

    public int getExtraResourceId(int resId) {
        return ResourceUtils.getExtraResId(mDefaultResourceSkin, resId, mCurrentResourceSkin, mExtraResourcePackageName);
    }


    public void loadAttrs(String activityName, View view, AttributeSet attrs) {

        Map<View, SkinAttrs> skinAttrsMap =  loadViewWithActivity(activityName);

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            if (mAttributes.contains(attrName)) {
                String attrValue = attrs.getAttributeValue(i);
                SkinAttrs skinAttrs = new SkinAttrs(attrName);
                skinAttrsMap.put(view,skinAttrs);

                int resId = 0;
                if (attrValue.startsWith("@")) {
                    resId = Integer.parseInt(attrValue.substring(1));
                } else if (attrValue.startsWith("?")) {
                    int attrsId = Integer.parseInt(attrValue.substring(1));
                    resId = ResourceUtils.getResIdByAttrsId(view.getContext(), new int[]{attrsId})[0];
                }
                skinAttrs.setAttrId(resId);
                updateSkin(view,skinAttrs);
            }
        }
    }

    private void updateApply() {
        for (Map.Entry<String, Map<View, SkinAttrs>> entries : mActivityManager.entrySet()) {
            Map<View, SkinAttrs> model = entries.getValue();
            for (Map.Entry<View, SkinAttrs> entry : model.entrySet()) {
                updateSkin(entry.getKey(),entry.getValue());
            }
        }
    }


    private void updateSkin(View view, SkinAttrs skinAttrs) {

        Resources resources = mCurrentResourceSkin;
        int resId = 0;
        if(!isDefaultSkin){
            resId = getExtraResourceId(skinAttrs.getAttrId());
            if(resId <= 0){
                resources = mDefaultResourceSkin;
                resId = skinAttrs.getAttrId();
            }
        }else {
           resId = skinAttrs.getAttrId();
           resources = mDefaultResourceSkin;
        }

        if(resId <= 0){
            return;
        }
        switch (skinAttrs.getAttrName()) {
            case "background":
                Drawable bg = null;
                try{
                    bg = resources.getDrawable(resId);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (null == bg) {
                    int color = 0;
                    try{
                        color =  resources.getColor(resId);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    view.setBackgroundColor(color);
                } else {
                    view.setBackground(bg);
                }
                break;
            case "src":

                break;
            case "textColor":
                break;
            case "drawableLeft":
                break;
            case "drawableRight":
                break;
            case "drawableBottom":
                break;
        }
    }

    public Map<View, SkinAttrs> loadViewWithActivity(String name) {
        Map<View, SkinAttrs> skinAttrMap;
        if (mActivityManager.containsKey(name)) {
            skinAttrMap = mActivityManager.get(name);
        } else {
            skinAttrMap = new HashMap<>();
            mActivityManager.put(name, skinAttrMap);
        }
        return skinAttrMap;
    }

    public void remove(String name) {
        mActivityManager.remove(name);
    }
}
