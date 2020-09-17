package com.conagra.hardware.database;


import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.text.TextUtils;

import com.conagra.hardware.database.api.SqlApi;
import com.conagra.hardware.model.FetalHeartRecordModel;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by yedongqin on 16/9/29.
 */
public class HeartRateDao extends BaseDao<FetalHeartRecordModel> {

    public HeartRateDao(Context context) {
        super(context);
    }

    @Override
    public int add(FetalHeartRecordModel heartBean) {

        if (heartBean == null)
            return -1;
        SQLiteDatabase db = sqlHelp.getWritableDatabase();
        String sql = "insert into " + SqlApi.DATABASE_TABLE_HEART_RATE + " ( "
                + SqlApi.HEART_RATE_CUSTOMERNO + ","         //平均心率
                + SqlApi.HEART_RATE_HOSPITALID + ","         //平均心率
                + SqlApi.HEART_RATE_HEARTRATE + ","         //平均心率
                + SqlApi.HEART_RATE_PATH_MUSIC + ","        //音频路径
                + SqlApi.HEART_RATE_STARTTIME + ","         //开始时间
                + SqlApi.HEART_RATE_AUDIO_FILE_SIZE + ","
                + SqlApi.HEART_RATE_PIONTS1 + ","
                + SqlApi.HEART_RATE_PIONTS2 + ","
                + SqlApi.HEART_RATE_TIME + ","
                + SqlApi.HEART_RATE_UPLOAD_STTUS + ","
                + SqlApi.HEART_RATE_OPEN_URL_MUSIC + ","
                + SqlApi.HEART_RATE_DOWNLOAD_URL_MUSIC + ","
                + SqlApi.HEART_RATE_MUSIC_NAME
                + ") values (\""       //一共时长
                + heartBean.getCustomerNo() + "\",\""
                + heartBean.getHospitalID() + "\",\""
                + heartBean.getAvgHeartRate() + "" + "\",\""         //
                + heartBean.getAudiofs() + "\",\""
                + heartBean.getStartTime() + "\",\""
                + heartBean.getAudioFileSize() + "" + "\",\""
                + heartBean.getPionts1() + "\",\""
                + heartBean.getPionts2() + "\",\""
                + heartBean.getTimes() + "\",\""
                + heartBean.getUploadStatus() + "\",\""
                + heartBean.getAudiofsUrl() + "\",\""
                + heartBean.getDownloadUrl() + "\",\""
                + heartBean.getFileName() + "" + "\");";
        db.execSQL(sql);
        db.close();

        List<FetalHeartRecordModel> fetalHeartRecordModels = selectAll();
        return selectFirstData();
    }

    @Override
    public void delete(String id) {
        SQLiteDatabase db = sqlHelp.getWritableDatabase();
        db.delete(SqlApi.DATABASE_TABLE_HEART_RATE, SqlApi.HEART_RATE_ID + "=?", new String[]{id});
        db.close();
    }

    public int selectFirstData(){
        SQLiteDatabase db = sqlHelp.getWritableDatabase();
        String sql = "select  " +
                SqlApi.HEART_RATE_ID +
                " from " + SqlApi.DATABASE_TABLE_HEART_RATE +
                " order by " + SqlApi.HEART_RATE_ID +
                "  desc limit 0,1";

//        db.execSQL();
        Cursor cursor = db.rawQuery(sql,null);
//       Cursor cursor =  db.query(SqlApi.DATABASE_TABLE_HEART_RATE,null,null,null,null,null,null);
        if(cursor != null &&cursor.moveToNext()&&cursor.moveToFirst()){
            int result = cursor.getInt(cursor.getColumnIndex(SqlApi.HEART_RATE_ID));
            cursor.close();
            db.close();
            return result;
        }
        cursor.close();
        db.close();
        return -1;
    }
    @Override
    public void update(int id, FetalHeartRecordModel heartBean) {
        SQLiteDatabase db = sqlHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlApi.HEART_RATE_ID,heartBean.getId());
        values.put(SqlApi.HEART_RATE_HEARTRATE,heartBean.getAvgHeartRate());
        values.put(SqlApi.HEART_RATE_PATH_MUSIC, heartBean.getAudiofs());
        values.put(SqlApi.HEART_RATE_STARTTIME,heartBean.getStartTime());
        values.put(SqlApi.HEART_RATE_TIME,heartBean.getAvgHeartRate() );
        values.put(SqlApi.HEART_RATE_AUDIO_FILE_SIZE,heartBean.getAudioFileSize());
        values.put(SqlApi.HEART_RATE_CUSTOMERNO,heartBean.getCustomerNo());
        values.put(SqlApi.HEART_RATE_HOSPITALID,heartBean.getHospitalID());
        values.put(SqlApi.HEART_RATE_OPEN_URL_MUSIC,heartBean.getAudiofsUrl());
        values.put(SqlApi.HEART_RATE_DOWNLOAD_URL_MUSIC,heartBean.getDownloadUrl());
        values.put(SqlApi.HEART_RATE_UPLOAD_STTUS,heartBean.getUploadStatus());
        values.put(SqlApi.HEART_RATE_MUSIC_NAME,heartBean.getFileName());
        values.put(SqlApi.HEART_RATE_PIONTS1,heartBean.getPionts1());
        values.put(SqlApi.HEART_RATE_PIONTS2,heartBean.getPionts2());
        db.update(SqlApi.DATABASE_TABLE_HEART_RATE, values, SqlApi.HEART_RATE_ID + "=?", new String[]{String.valueOf(heartBean.getId())});
        db.close();

    }

    public String selectPathByFileName(String fileName){
        if(!TextUtils.isEmpty(fileName)){
            SQLiteDatabase db = sqlHelp.getReadableDatabase();
            String sql = "select " + SqlApi.HEART_RATE_PATH_MUSIC + " from " + SqlApi.DATABASE_TABLE_HEART_RATE
                    + " where " + SqlApi.HEART_RATE_MUSIC_NAME + " = \"" + fileName + "\";";
            Cursor cursor = db.rawQuery(sql,null);
            String result = null;
            if(cursor != null && cursor.moveToFirst()){
                result = cursor.getString(cursor.getColumnIndex(SqlApi.HEART_RATE_PATH_MUSIC));
            }
            cursor.close();
            db.close();
            return result;
        }
        return null;
    }

    @Override
    public FetalHeartRecordModel select(String  id) {

        SQLiteDatabase db = sqlHelp.getReadableDatabase();
        Cursor cursor = db.query(SqlApi.DATABASE_TABLE_HEART_RATE, null, SqlApi.HEART_RATE_ID + "=?", new String[]{id}, null, null, null);
        if(cursor != null && cursor.moveToFirst()){
            FetalHeartRecordModel heartBean = getFetalHeartRecordBean(cursor);
            return heartBean;
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private FetalHeartRecordModel getFetalHeartRecordBean(Cursor cursor){
        FetalHeartRecordModel heartBean = new FetalHeartRecordModel();
        int id = cursor.getInt(cursor.getColumnIndex(SqlApi.HEART_RATE_ID));
        heartBean.setId(id);
//        String s = cursor.getString(cursor.getColumnIndex(SqlApi.HEART_RATE_HEARTRATE));
        int heartrate = cursor.getInt(cursor.getColumnIndex(SqlApi.HEART_RATE_HEARTRATE));
        heartBean.setAvgHeartRate(heartrate);
        heartBean.setAudiofs(cursor.getString(cursor.getColumnIndex(SqlApi.HEART_RATE_PATH_MUSIC)));
        heartBean.setStartTime(cursor.getString(cursor.getColumnIndex(SqlApi.HEART_RATE_STARTTIME)));
        heartBean.setTimes(cursor.getInt(cursor.getColumnIndex(SqlApi.HEART_RATE_TIME)));
        heartBean.setPionts2(cursor.getString(cursor.getColumnIndex(SqlApi.HEART_RATE_PIONTS2)));
        heartBean.setPionts1(cursor.getString(cursor.getColumnIndex(SqlApi.HEART_RATE_PIONTS1)));
        heartBean.setAudioFileSize(cursor.getInt(cursor.getColumnIndex(SqlApi.HEART_RATE_AUDIO_FILE_SIZE)));
        heartBean.setAudiofsUrl(cursor.getString(cursor.getColumnIndex(SqlApi.HEART_RATE_OPEN_URL_MUSIC)));
        heartBean.setDownloadUrl(cursor.getString(cursor.getColumnIndex(SqlApi.HEART_RATE_DOWNLOAD_URL_MUSIC)));
        heartBean.setFileName(cursor.getString(cursor.getColumnIndex(SqlApi.HEART_RATE_MUSIC_NAME)));
        heartBean.setUploadStatus(cursor.getInt(cursor.getColumnIndex(SqlApi.HEART_RATE_UPLOAD_STTUS)));
        heartBean.setCustomerNo(cursor.getString(cursor.getColumnIndex(SqlApi.HEART_RATE_CUSTOMERNO)));
        heartBean.setHospitalID(cursor.getString(cursor.getColumnIndex(SqlApi.HEART_RATE_HOSPITALID)));

//        try {
//            String point1 = new String(Base64.decode(cursor.getString(cursor.getColumnIndex(SqlApi.HEART_RATE_PIONTS1)),Base64.NO_WRAP),"UTF-8");
//            JSONArray jsonArray = new JSONArray(point1);
//            List<String> list = new ArrayList<>();
//
//            for (int i = 0; i < jsonArray.length(); i++){
//                list.add(jsonArray.getString(i));
//            }
////            heartBean.setPionts1(list);
//
//            List<String> list1 = new ArrayList<>();
//            String point2 = new String(Base64.decode(cursor.getString(cursor.getColumnIndex(SqlApi.HEART_RATE_PIONTS2)),Base64.NO_WRAP),"UTF-8");
//            JSONArray jsonArray1 = new JSONArray(point2);
//            for (int i = 0; i < jsonArray1.length(); i++){
//                list1.add(jsonArray1.getString(i));
//            }
//            heartBean.setPionts2(list1);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        return heartBean;
    }

    @Override
    public List<FetalHeartRecordModel> selectAll() {

        LinkedList<FetalHeartRecordModel> heartBeenList = null;
        SQLiteDatabase db = sqlHelp.getReadableDatabase();
        Cursor cursor = db.query(SqlApi.DATABASE_TABLE_HEART_RATE, null, null, null, null, null, null);
        if(cursor != null){
            heartBeenList = new LinkedList<>();
            while(cursor.moveToNext()){
                FetalHeartRecordModel heartBean = getFetalHeartRecordBean(cursor);
                heartBeenList.push(heartBean);
            }
        }
        cursor.close();
        db.close();
        return heartBeenList;
    }
}

