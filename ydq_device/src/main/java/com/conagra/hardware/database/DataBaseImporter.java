package com.conagra.hardware.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by yedongqin on 2016/11/2.
 */

public class DataBaseImporter {

    public static final String PACKAGE_NAME = "com.example.sql";
    public static final String DB_NAME = "xxx.db";
    public static String DB_PATH = "/data/data/" + PACKAGE_NAME;
    private Context context;

    public DataBaseImporter(Context mContext) {
        this.context = mContext;
    }

    public SQLiteDatabase openDataBase() {
        return SQLiteDatabase.openOrCreateDatabase(DB_PATH + "/" + DB_NAME, null);
    }

    public void copyDB() {
        File file = new File(DB_PATH + "/" + DB_NAME);
//        if (!file.exists()) {
//            try {
//                FileOutputStream out = new FileOutputStream(file);
//                int buffer = 400000;
//                // 读取数据库并保存到data/data/packagename/xx.db...
//                InputStream ins = context.getResources().openRawResource(R.raw.heartrate);
//                byte[] bts = new byte[buffer];
//                int length;
//                while ((length = ins.read(bts)) > 0) {
//                    out.write(bts, 0, bts.length);
//                }
//                out.close();
//                ins.close();
//                SQLiteDatabase.openOrCreateDatabase(DB_PATH + "/" + DB_NAME, null);
//            } catch (FileNotFoundException e) {
//            } catch (IOException e) {
//            }
//        }
    }
    
}
