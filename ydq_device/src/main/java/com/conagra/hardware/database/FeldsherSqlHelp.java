package com.conagra.hardware.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.conagra.hardware.database.api.SqlApi;


/**
 * Created by yedongqin on 16/9/29.
 */
public class FeldsherSqlHelp extends SQLiteOpenHelper {
    private Context context;

    public FeldsherSqlHelp(Context context) {
        super(context, SqlApi.DATABASE_NAME, null, SqlApi.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqlApi.HEART_RATE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SqlApi.HEART_RATE_TABLE_DROP);
        onCreate(db);
    }

}
