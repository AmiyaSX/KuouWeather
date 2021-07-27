package com.example.kuouweather.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * 实现SQLiteOpenHelper抽象类，对数据库进行操作 (工具类 采用单例模式)
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static DBOpenHelper mInstance;

    public static DBOpenHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBOpenHelper(context);
        }
        return mInstance;
    }

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "sql.db";
    public static final String TABLE_NAME_1 = "province";
    public static final String TABLE_NAME_2 = "city";
    public static final String TABLE_NAME_3 = "county";

    /*构造函数私有化*/
    private DBOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*创建表（初始化数据库）*/
        String sql1 = "create table if not exists " + TABLE_NAME_1 + "(_id integer primary key autoincrement, name text, pro_id integer);";
        db.execSQL(sql1);
        String sql2 = "create table if not exists " + TABLE_NAME_2 + "(_id integer primary key autoincrement, name text, city_id integer);";
        db.execSQL(sql2);
        String sql3 = "create table if not exists " + TABLE_NAME_3 + "(_id integer primary key autoincrement, name text, weather_id text);";
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*升级sql*/

    }

}
