package com.example.kuouweather.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteManager {
    Context context;

    public SqliteManager(Context context) {
        this.context = context;
    }

    public void query() {
        SQLiteOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
        SQLiteDatabase readableDatabase = dbOpenHelper.getReadableDatabase();

        if (readableDatabase.isOpen()) {
            Cursor cursor = readableDatabase.rawQuery("select * from " + DBOpenHelper.TABLE_NAME_1, null);
            /*迭代游标*/
            while (cursor.moveToNext()) {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
            }
            cursor.close();
            readableDatabase.close();
        }

    }

    public void insert() {
        SQLiteOpenHelper helper = DBOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        if (db.isOpen()) {
            String sql = "insert into " + DBOpenHelper.TABLE_NAME_1 + "(name)" + "values('')";
            db.execSQL(sql);
        }
        db.close();
    }

    public void update() {
        SQLiteOpenHelper helper = DBOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        if (db.isOpen()) {
            String sql = "update " + DBOpenHelper.TABLE_NAME_1 + " set name = ? where _id = ?";
            db.execSQL(sql, new Object[]{"", 1});    //对?的补全
        }
        db.close();
    }

    public void delete() {
        SQLiteOpenHelper helper = DBOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        if (db.isOpen()) {
            String sql = "delete from " + DBOpenHelper.TABLE_NAME_1 + " where _id = ?";
            db.execSQL(sql, new Object[]{1});    //对?的补全
        }

        db.close();
    }

}
