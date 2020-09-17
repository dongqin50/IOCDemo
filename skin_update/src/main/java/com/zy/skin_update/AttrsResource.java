package com.zy.skin_update;

import android.util.AttributeSet;
import android.view.View;

import com.zy.skin_update.utils.ResourceUtils;
import com.zy.skin_update.utils.SkinManager;

import java.util.ArrayList;
import java.util.List;

public class AttrsResource {

    private static final List<String> mAttributes = new ArrayList<>();

    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
    }

    public static void loadAttrs(View view, AttributeSet attrs){

        for(int i = 0 ; i < attrs.getAttributeCount(); i++){
            String attrName = attrs.getAttributeName(i);
            if(mAttributes.contains(attrName)){
                String attrValue = attrs.getAttributeValue(i);
                int resId;
                if(attrValue.startsWith("@") ){
                    resId = Integer.parseInt(attrValue.substring(1));
                }else if(attrValue.startsWith("?")){
                    int attrsId = Integer.parseInt(attrValue.substring(1));
                    resId = ResourceUtils.getResIdByAttrsId(view.getContext(),new int[]{attrsId})[0];
                }
                if(SkinManager.getInstance().isDefaultSkin()){


                }else {

                }

            }
        }

    }



}
