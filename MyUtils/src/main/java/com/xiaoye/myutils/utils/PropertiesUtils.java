package com.xiaoye.myutils.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by yedongqin on 2017/4/12.
 * 通过配置文件设置属性
 */

public class PropertiesUtils {

    private static PropertiesUtils mPropertiesUtils;
    private static Properties mProperties;
    private InputStream inputS;

    public static PropertiesUtils getInstance(String name){

        if(mPropertiesUtils == null){
            synchronized (PropertiesUtils.class){
                if(mPropertiesUtils == null){
                    mPropertiesUtils = new PropertiesUtils();
                    mProperties = new Properties();
                }
            }
        }
        mPropertiesUtils.init(name);
        return mPropertiesUtils;
    }

    private void init(String name){
        if(inputS == null){
            inputS = getClass().getResourceAsStream("/assets/"+name+".properties");
            try {
                if(inputS != null){
                    mProperties.load(inputS);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取属性
     * @return
     */
    public Properties getProperties() {
        return mProperties;
    }

}
