package com.example.kuouweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    DBOpenHelper dbOpenHelper = new DBOpenHelper(this,"data.db",null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SQLiteDatabase sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}