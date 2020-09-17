package com.conagra.cache;

/**
 * Created by yedongqin on 2018/4/22.
 */

public class Cache {

    private static String userId;
    private static String hospitalName;
    private static String serverIp;
    private static String serverPort;
    private static String token;


    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        Cache.userId = userId;
    }

    public static String getHospitalName() {
        return hospitalName;
    }

    public static void setHospitalName(String hospitalName) {
        Cache.hospitalName = hospitalName;
    }

    public static String getServerIp() {
        return serverIp;
    }

    public static String getServerPort() {
        return serverPort;
    }


    public static void setServerAddress(String serverIp,String serverPort) {
        Cache.serverIp = serverIp;
        Cache.serverPort = serverPort;
    }

    public static String getServerAddress() {

        return "http://"+serverIp+":" + serverPort + "/";
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Cache.token = token;
    }
}
